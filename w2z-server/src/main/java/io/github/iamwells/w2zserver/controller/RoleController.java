package io.github.iamwells.w2zserver.controller;


import io.github.iamwells.w2zserver.domain.Role;
import io.github.iamwells.w2zserver.service.RoleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/{id}")
    public Role get(@PathVariable("id") Integer id) {
        return roleService.getById(id);
    }
}
