package com.pms4st.pms.service;

import com.pms4st.pms.entity.*;
import com.pms4st.pms.exception.ResourceNotFoundException;
import com.pms4st.pms.repository.*;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service // Mark as Spring service
public class AppService {

    // Inject all repositories needed
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private TaskRepository taskRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private PasswordEncoder passwordEncoder; // For registration

    // === User Methods ===

    @Transactional
    public User registerUser(String username, String rawPassword, String email, String fullName) {
        if (userRepository.existsByUsername(username)) throw new IllegalArgumentException("Username taken");
        if (userRepository.existsByEmail(email)) throw new IllegalArgumentException("Email registered");

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword)); // Hash password
        user.setEmail(email);
        user.setFullName(fullName);
        user.setEnabled(true);
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("ROLE_USER missing"));
        user.setRoles(new HashSet<>(Set.of(userRole)));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found ID: " + userId));
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // === Project Methods ===

    @Transactional(readOnly = true)
    public List<Project> findProjectsForUser(String username) {
        User user = findUserByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return projectRepository.findByMemberId(user.getId());
    }

    @Transactional(readOnly = true)
    public Project findProjectById(Long projectId) {
         return projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found ID: " + projectId));
    }

     // Finds project AND checks if user can access it
     @Transactional(readOnly = true)
    public Project findProjectByIdForUser(Long projectId, String username) {
         Project project = findProjectById(projectId);
         User user = findUserByUsername(username).orElseThrow(); // User must exist if authenticated
         checkProjectAccess(project, user); // Use helper below
         Hibernate.initialize(project.getMembers()); // Force load members
         return project;
    }

    @Transactional
    public Project saveProject(Project project, String ownerUsername) {
        User owner = findUserByUsername(ownerUsername).orElseThrow(); // Should exist
        project.setOwner(owner);

        if (project.getId() == null) { // Creating
            Set<User> members = new HashSet<>();
            members.add(owner);
            project.setMembers(members);
            if (project.getStartDate() == null) project.setStartDate(LocalDate.now());
        } else { // Updating
             Project existing = findProjectById(project.getId());
             checkProjectAccess(existing, owner); // Verify owner is editing
             project.setMembers(existing.getMembers()); // Keep existing members
             project.setOwner(existing.getOwner()); // Owner cannot be changed here
        }
        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long projectId, String username) {
        Project project = findProjectById(projectId);
        User user = findUserByUsername(username).orElseThrow();
        if (!project.getOwner().getId().equals(user.getId())) { // Only owner can delete
            throw new AccessDeniedException("Only owner can delete project");
        }
        // Manual cleanup for project-level comments
        commentRepository.deleteByProjectIdAndTaskIsNull(projectId);
        // Delete project (DB cascade handles tasks, memberships, task-comments)
        projectRepository.deleteById(projectId);
    }

    // === Member Methods ===
     @Transactional
    public void addMemberToProject(Long projectId, Long userIdToAdd, String currentUsername) {
        Project project = findProjectByIdForUser(projectId, currentUsername); // Checks access
        User userToAdd = findUserById(userIdToAdd);
        if (project.getMembers().add(userToAdd)) projectRepository.save(project);
    }

    @Transactional
    public void removeMemberFromProject(Long projectId, Long userIdToRemove, String currentUsername) {
        Project project = findProjectByIdForUser(projectId, currentUsername); // Checks access
        User userToRemove = findUserById(userIdToRemove);
        if (project.getOwner().getId().equals(userToRemove.getId())) throw new IllegalArgumentException("Cannot remove owner");
        if (project.getMembers().remove(userToRemove)) projectRepository.save(project);
    }

     @Transactional(readOnly = true)
    public List<User> findPotentialMembers(Long projectId) {
        Project project = findProjectById(projectId);
        List<User> allUsers = findAllUsers();
        Set<Long> memberIds = project.getMembers().stream().map(User::getId).collect(Collectors.toSet());
        return allUsers.stream().filter(u -> !memberIds.contains(u.getId())).collect(Collectors.toList());
    }


    // === Task Methods ===
    @Transactional(readOnly = true)
    public List<Task> findTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectIdOrderByIdAsc(projectId);
    }

    @Transactional(readOnly = true)
    public Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found ID: " + taskId));
    }

     @Transactional(readOnly = true)
    public Task findTaskByIdForUser(Long taskId, String username) {
        Task task = findTaskById(taskId);
        User user = findUserByUsername(username).orElseThrow();
        checkProjectAccess(task.getProject(), user); // Check access via project
        return task;
    }

     @Transactional
    public Task saveTask(Task task, Long projectId, String creatorUsername) {
         Project project = findProjectById(projectId);
         User creator = findUserByUsername(creatorUsername).orElseThrow();
         checkProjectAccess(project, creator); // Check creator access

         task.setProject(project);
         task.setCreatedBy(creator);

         // Handle assignee simply
         User assignee = null; // Declare here
         if (task.getAssignee() != null && task.getAssignee().getId() != null) {
             // Find the potential assignee FIRST
             User potentialAssignee = findUserById(task.getAssignee().getId());
 
             // Check if assignee is member or owner using the potentialAssignee directly
             boolean isAssigneeMember = project.getMembers().stream()
                 .anyMatch(m -> m.getId().equals(potentialAssignee.getId())); // Use potentialAssignee here
             if (!project.getOwner().getId().equals(potentialAssignee.getId()) && !isAssigneeMember){
                  throw new IllegalArgumentException("Assignee must be a project member or owner.");
             }
             // If the check passes, assign it
             assignee = potentialAssignee;
         }
         task.setAssignee(assignee); // Set the final result (either null or the valid user)

         if (task.getStatus() == null) task.setStatus("TODO");
         if (task.getPriority() == null) task.setPriority("MEDIUM");

         return taskRepository.save(task);
    }

     @Transactional
    public Task updateTask(Task taskDetails, Long taskId, String username) {
        Task existingTask = findTaskByIdForUser(taskId, username); // Check access
        Project project = existingTask.getProject();

        // Update fields
        existingTask.setName(taskDetails.getName());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setStatus(taskDetails.getStatus());
        existingTask.setPriority(taskDetails.getPriority());
        existingTask.setDeadline(taskDetails.getDeadline());

         // Handle assignee update
         User assignee = null; // Declare here
         if (taskDetails.getAssignee() != null && taskDetails.getAssignee().getId() != null) {
             // Find the potential assignee FIRST
             User potentialAssignee = findUserById(taskDetails.getAssignee().getId());

             // Check if assignee is member using the potentialAssignee directly
             boolean isAssigneeMember = existingTask.getProject().getMembers().stream() // Use existingTask.getProject()
                 .anyMatch(m -> m.getId().equals(potentialAssignee.getId())); // Use potentialAssignee here
             if (!existingTask.getProject().getOwner().getId().equals(potentialAssignee.getId()) && !isAssigneeMember){
                 throw new IllegalArgumentException("Assignee must be a project member or owner.");
             }
             // If check passes, assign it
             assignee = potentialAssignee;
         }
        existingTask.setAssignee(assignee); // Set the final result on the existingTask

        return taskRepository.save(existingTask);
    }

    @Transactional
    public void deleteTask(Long taskId, String username) {
        Task task = findTaskByIdForUser(taskId, username); // Check access
         // Optional: Add finer check (creator/owner?)
        taskRepository.deleteById(taskId); // DB Cascade handles task comments
    }

    // === Comment Methods ===
    @Transactional(readOnly = true)
    public List<Comment> findCommentsForProject(Long projectId) {
        return commentRepository.findByProjectIdAndTaskIsNullOrderByCreatedAtDesc(projectId);
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentsForTask(Long taskId) {
        return commentRepository.findByTaskIdOrderByCreatedAtDesc(taskId);
    }

    @Transactional
    public Comment addComment(String content, Long projectId, Long taskId, String username) {
        User user = findUserByUsername(username).orElseThrow();
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);

        if (taskId != null) { // Task comment
            Task task = findTaskByIdForUser(taskId, username); // Checks access via project
            comment.setTask(task);
        } else if (projectId != null) { // Project comment
            Project project = findProjectByIdForUser(projectId, username); // Checks access
            comment.setProject(project);
        } else {
            throw new IllegalArgumentException("Comment needs project or task ID.");
        }
        return commentRepository.save(comment);
    }

    // --- Helper Method ---
    private void checkProjectAccess(Project project, User user) {
        if (project == null) throw new ResourceNotFoundException("Project not found for access check.");
        boolean isMember = project.getMembers().stream().anyMatch(m -> m.getId().equals(user.getId()));
        if (!project.getOwner().getId().equals(user.getId()) && !isMember) {
            throw new AccessDeniedException("User '" + user.getUsername() + "' cannot access project ID " + project.getId());
        }
    }
}