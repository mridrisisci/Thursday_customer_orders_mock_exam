package app.entities;

import app.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "user_account")
public class UserAccount implements ISecurityUser
{
    @Id
    private String username;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles = new HashSet<>();

    public UserAccount(String userName, String userPass)
    {
        this.username = userName;
        this.password = BCrypt.hashpw(userPass, BCrypt.gensalt());
    }

    public UserAccount(String userName, Set<Roles> roleEntityList)
    {
        this.username = userName;
        this.roles = roleEntityList;
    }

    @Override
    public Set<String> getRolesAsStrings()
    {
        return roles.stream().map(Roles::toString).collect(Collectors.toSet());
    }

    @Override
    public boolean verifyPassword(String pw)
    {
        return BCrypt.checkpw(pw, this.password);
    }

    @Override
    public void addRole(Roles role)
    {
        if (role != null)
        {
            roles.add(role);
        }
    }
    @Override
    public void removeRole(Roles roleName)
    {
        //roles.remove(Roles.valueOf(roleName.toUpperCase()));
        roles.removeIf(r -> r.toString().equals(roleName));
    }

    @Override
    public Set<Roles> getRoles()
    {
        return roles;
    }


}
