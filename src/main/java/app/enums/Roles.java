package app.enums;

import io.javalin.security.RouteRole;

public enum Roles implements RouteRole
{
    ANYONE,
    USER_READ,
    USER_WRITE,
    ADMIN
}