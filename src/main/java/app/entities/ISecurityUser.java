package app.entities;

import app.enums.Roles;

import java.util.Set;

public interface ISecurityUser {
    Set<Roles> getRoles();
    Set<String> getRolesAsStrings();
    boolean verifyPassword(String pw);
    void addRole(Roles role);
    void removeRole(Roles role);

}