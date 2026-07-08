package org.example.authservice.domain.model;

import org.example.authservice.domain.constant.Action;
import org.example.authservice.domain.constant.Resource;
import org.example.authservice.domain.constant.Scope;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Permission {
    private UUID id;
    private Resource resource;
    private Action action;
    private Scope scope;

    public Permission(UUID id, Resource resource, Action action, Scope scope) {
        this.id = id;
        this.resource = resource;
        this.action = action;
        this.scope = scope;
    }

    public Permission() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public String toAuthority() {
        if (resource == null || action == null || scope == null) {
            return "";
        }
        return resource.name() + ":" + action.name() + "_" + scope.name();
    }
}