package sk.zatko.dp.data.controllers;

import lombok.Data;

@Data
public class Controller {

    private String methodName;
    private String requestMapping;
    private String httpMethod;
    private String className;
    private String classFilePath;
    private boolean alwaysAccessible;
    private boolean pageController;
    private boolean restController;
    private String methodDeclaration;
    private String methodBody;
}
