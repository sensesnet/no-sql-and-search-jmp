package org.epam.jmp.controllers.ui;

import org.epam.jmp.models.Category;
import org.springframework.stereotype.Controller;

@Controller
public class CUIController implements UIController{

    @Override
    public void displayMenu() {

        var menu = """
                1. Display on console all tasks.
                2. Display overdue tasks.
                3. Display all tasks with a specific category.
                4. Display all subtasks related to tasks with a specific category.
                5. Create task.
                6. Update task.
                7. Delete task.
                8. Create subtask.
                9. Update subtask.
                10. Delete subtask.
                11. Search tasks by description.
                12. Search sub-tasks by name. 
                13. Exit.
                """;

        System.out.println("Choose option:");
        System.out.println(menu);
    }

    @Override
    public void displayString(String info) {
        System.out.println(info);
    }

    @Override
    public void displayCategories() {
        System.out.println("Choose category:");
        for (int i = 0; i < Category.values().length; i++) {
            System.out.println(i + 1 + ". " + Category.values()[i]);
        }
    }
}
