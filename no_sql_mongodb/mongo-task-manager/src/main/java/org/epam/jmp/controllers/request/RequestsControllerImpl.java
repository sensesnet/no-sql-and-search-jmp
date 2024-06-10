package org.epam.jmp.controllers.request;

import org.epam.jmp.controllers.ui.UIController;
import org.epam.jmp.models.Category;
import org.epam.jmp.services.TaskService;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

@Controller
public class RequestsControllerImpl implements RequestController{

    public static final String INCORRECT_INPUT_TRY_AGAIN = "Incorrect input. Try again";
    public static final String ENTER_TASK_ID_UUID_FORMAT = "Enter task id (uuid format):";
    public static final String NOT_EXISTS = " not exists.";
    public static final String USER_WITH_ID = "User with id ";
    private final UIController uiController;
    private final TaskService taskService;

    public RequestsControllerImpl(UIController uiController, TaskService taskService) {
        this.uiController = uiController;
        this.taskService = taskService;
    }

    @Override
    public void startListenUserRequests() {
        uiController.displayMenu();

        while (true) {
            var scaner = new Scanner(System.in);

            switch (scaner.nextInt()) {
                case 1:
                    uiController.displayString(taskService.findAllTasks());
                    uiController.displayMenu();
                    break;
                case 2:
                    uiController.displayString(taskService.findOverdueTasks());
                    uiController.displayMenu();
                    break;
                case 3:
                    uiController.displayString(processFindTasksByCategory());
                    uiController.displayMenu();
                    break;
                case 4:
                    uiController.displayString(processFindSubtasksByCategory());
                    uiController.displayMenu();
                    break;
                case 5:
                    uiController.displayString(processCreateTask());
                    uiController.displayMenu();
                    break;
                case 6:
                    uiController.displayString(processUpdateTask());
                    uiController.displayMenu();
                    break;
                case 7:
                    uiController.displayString(processDeleteTask());
                    uiController.displayMenu();
                    break;
                case 8:
                    uiController.displayString(processCreateSubtask());
                    uiController.displayMenu();
                    break;
                case 9:
                    uiController.displayString(processUpdateSubtask());
                    uiController.displayMenu();
                    break;
                case 10:
                    uiController.displayString(processDeleteSubtask());
                    uiController.displayMenu();
                    break;
                case 11:
                    uiController.displayString(processSearchTaskByDesk());
                    uiController.displayMenu();
                    break;
                case 12:
                    uiController.displayString(processSearchSubtaskByName());
                    uiController.displayMenu();
                    break;
                case 13:
                    uiController.displayString("Shutting down...");
                    return;
                default:
                    uiController.displayString(INCORRECT_INPUT_TRY_AGAIN);
                    uiController.displayMenu();
                    break;
            }
        }
    }

    private String processCreateTask() {
        var scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        uiController.displayString("Enter task deadline (format: \"dd-MM-yyyy HH:mm:ss\"):");
        LocalDateTime deadline = LocalDateTime.parse(scanner.nextLine(), formatter);

        uiController.displayString("Enter task name:");
        String taskName = scanner.nextLine();

        uiController.displayString("Enter task description:");
        String taskDescription = scanner.nextLine();

        uiController.displayCategories();
        Category category = null;
        while (category == null) {
            switch (scanner.nextInt()){
                case 1:
                    category = Category.IMPLEMENTATION;
                    break;
                case 2:
                    category = Category.TESTING;
                    break;
                case 3:
                    category = Category.BUG_FIXING;
                    break;
                case 4:
                    category = Category.DEPLOYING;
                    break;
                default:
                    uiController.displayString(INCORRECT_INPUT_TRY_AGAIN);
            }
        }

        return taskService.createTask(deadline, taskName, taskDescription, category);
    }

    private String processUpdateTask() {
        Map<Integer, String> updateRequest = new HashMap<>();
        var scanner = new Scanner(System.in);
        uiController.displayString(ENTER_TASK_ID_UUID_FORMAT);
        UUID id = UUID.fromString(scanner.nextLine());
        if (!taskService.isTaskExists(id)){
            return USER_WITH_ID + id + NOT_EXISTS;
        }

        uiController.displayString("Enter task deadline (format: \"dd-MM-yyyy HH:mm:ss\") or skip (press ENTER):");
        var newDeadLine = scanner.nextLine();
        updateRequest.put(1, newDeadLine);

        uiController.displayString("Enter new name or skip (press ENTER):");
        var newName = scanner.nextLine();
        updateRequest.put(2, newName);

        uiController.displayString("Enter new description or skip (press ENTER):");
        var newDescription = scanner.nextLine();
        updateRequest.put(3, newDescription);

        return taskService.updateTask(id, updateRequest);
    }

    private String processDeleteTask() {
        var scanner = new Scanner(System.in);
        uiController.displayString(ENTER_TASK_ID_UUID_FORMAT);
        UUID id = UUID.fromString(scanner.nextLine());
        if (!taskService.isTaskExists(id)){
            return USER_WITH_ID + id + NOT_EXISTS;
        }
        return taskService.deleteTask(id);
    }

    private String processCreateSubtask() {
        var scanner = new Scanner(System.in);
        uiController.displayString(ENTER_TASK_ID_UUID_FORMAT);
        UUID id = UUID.fromString(scanner.nextLine());
        if (!taskService.isTaskExists(id)){
            return USER_WITH_ID + id + NOT_EXISTS;
        }

        uiController.displayString("Enter sub-task name:");
        var name = scanner.nextLine();

        uiController.displayString("Enter sub-task description:");
        var description = scanner.nextLine();

        return taskService.createSubtask(name, description, id);
    }

    private String processUpdateSubtask() {
        var scanner = new Scanner(System.in);
        uiController.displayString(ENTER_TASK_ID_UUID_FORMAT);
        UUID id = UUID.fromString(scanner.nextLine());
        if (!taskService.isTaskExists(id)){
            return USER_WITH_ID + id + NOT_EXISTS;
        }

        uiController.displayString("Enter sub-task old name:");
        var oldName = scanner.nextLine();

        uiController.displayString("Enter sub-task new name:");
        var newName = scanner.nextLine();

        uiController.displayString("Enter sub-task new description:");
        var newDescription = scanner.nextLine();

        return taskService.updateSubtask(oldName, newName, newDescription, id);
    }

    private String processDeleteSubtask() {
        var scanner = new Scanner(System.in);
        uiController.displayString(ENTER_TASK_ID_UUID_FORMAT);
        UUID id = UUID.fromString(scanner.nextLine());
        if (!taskService.isTaskExists(id)){
            return USER_WITH_ID + id + NOT_EXISTS;
        }

        uiController.displayString("Enter sub-task name:");
        var name = scanner.nextLine();

        return taskService.deleteSubtaskByName(name, id);
    }

    private String processSearchTaskByDesk() {
        var scanner = new Scanner(System.in);

        uiController.displayString("Enter part of task description:");
        return taskService.findTasksByDescription(scanner.nextLine());
    }

    private String processSearchSubtaskByName() {
        var scanner = new Scanner(System.in);

        uiController.displayString("Enter part of sub-task name:");
        return taskService.findSubtasksByName(scanner.nextLine());
    }

    private String processFindSubtasksByCategory() {
        var scanner = new Scanner(System.in);
        uiController.displayCategories();
        return switch (scanner.nextInt()) {
            case 1 -> taskService.findAllSubtasksByTaskCategory(Category.IMPLEMENTATION);
            case 2 -> taskService.findAllSubtasksByTaskCategory(Category.TESTING);
            case 3 -> taskService.findAllSubtasksByTaskCategory(Category.BUG_FIXING);
            case 4 -> taskService.findAllSubtasksByTaskCategory(Category.DEPLOYING);
            default -> INCORRECT_INPUT_TRY_AGAIN;
        };
    }

    private String processFindTasksByCategory() {
        var scanner = new Scanner(System.in);
        uiController.displayCategories();
        return switch (scanner.nextInt()) {
            case 1 -> taskService.findAllTasksByCategory(Category.IMPLEMENTATION);
            case 2 -> taskService.findAllTasksByCategory(Category.TESTING);
            case 3 -> taskService.findAllTasksByCategory(Category.BUG_FIXING);
            case 4 -> taskService.findAllTasksByCategory(Category.DEPLOYING);
            default -> INCORRECT_INPUT_TRY_AGAIN;
        };
    }
}
