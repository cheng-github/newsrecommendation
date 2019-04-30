package cn.edu.usst.security;


import cn.edu.usst.service.AuthenticationService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component(value = "securityRealm")
public class SecurityRealm extends AuthorizingRealm {

    @Autowired
    AuthenticationService authenticationService;

    /**
     * 权限检查
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String username = String.valueOf(principals.getPrimaryPrincipal());
//        final User user = userService.selectByUsername(username);
//        final List<Role> roleInfos = roleService.selectRolesByUserId(user.getId());
//        for (Role role : roleInfos) {
//            // 添加角色
//            System.err.println(role);
//            authorizationInfo.addRole(role.getRoleSign());
//
//            final List<Permission> permissions = permissionService.selectPermissionsByRoleId(role.getId());
//            for (Permission permission : permissions) {
//                // 添加权限
//                System.err.println(permission);
//                authorizationInfo.addStringPermission(permission.getPermissionSign());
//            }
//        }
        return authorizationInfo;
    }

    /**
     * 登录验证，我们只需要登录验证即可
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = String.valueOf(token.getPrincipal());
        String password = new String((char[]) token.getCredentials());
        // 通过数据库进行验证, 我们不需要太多复杂的什么盐加密，仅客户端加密就可满足我们的需求
        String user__uuid = authenticationService.userLogin(username, password);
        if (user__uuid == null) {
            throw new AuthenticationException("用户名或密码错误.");
        }
        // 保留uuid和username
        SecurityUtils.getSubject().getSession().setAttribute("uuid", user__uuid);
        SecurityUtils.getSubject().getSession().setAttribute("username", username);
        SimpleAuthenticationInfo authenticationInfo =
                new SimpleAuthenticationInfo(username, password, getName());
        return authenticationInfo;
    }

    @Override
    public String getName() {
        return "userAuthentication";
    }
}
