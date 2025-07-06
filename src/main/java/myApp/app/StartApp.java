package myApp.app;

import myApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import myApp.service.UserService;

import java.util.Scanner;
import java.util.logging.Logger;

@Component
public class StartApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(StartApp.class.getName());
    private final UserService userService;

    @Autowired
    public StartApp(@Qualifier("userServiceImpl") UserService userService) {
        this.userService = userService;
    }

    public void start() {
        System.out.println("Выберите операцию работы с user:\n 1 - Создание.\n 2 - Изменение. \n 3 - Удаление. \n 4 - Чтение.");
        switch(scanner.nextInt()) {
            case 1: {
                scanner.nextLine();
                System.out.println("Напишите name нового user:");
                String name = scanner.nextLine();
                System.out.println("Напишите email нового user:");
                String email = scanner.nextLine();
                System.out.println("Напишите age нового user:");
                Integer age = scanner.nextInt();
                userService.create(name, email, age);
                break;
            }
            case 2: {
                scanner.nextLine();
                System.out.println("Напишите id user для изменения:");
                Long userId = scanner.nextLong();
                scanner.nextLine();
                User findUser = userService.read(userId);
                if (findUser != null) {
                    System.out.println("Напишите новый name:");
                    String name = scanner.nextLine();
                    System.out.println("Напишите новый email:");
                    String email = scanner.nextLine();
                    System.out.println("Напишите новый age:");
                    Integer age = scanner.nextInt();
                    scanner.nextLine();
                    userService.update(name, email, age, findUser);
                }
                break;
            }
            case 3: {
                scanner.nextLine();
                System.out.println("Напишите id нужного user:");
                Long userId = scanner.nextLong();
                userService.delete(userId);
                break;
            }
            case 4: {
                scanner.nextLine();
                System.out.println("Напишите id нужного user:");
                Long userId = scanner.nextLong();
                System.out.println(userService.read(userId));
                break;
            }
            default: {
                logger.warning("Введено неверное значение в главном меню выбора");
            }
        }
        scanner.close();
    }
}
