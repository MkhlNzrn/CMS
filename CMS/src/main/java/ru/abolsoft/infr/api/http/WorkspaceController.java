package ru.abolsoft.infr.api.http;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.abolsoft.core.common.exceptions.NotImplementedException;

@RequestMapping("workspaces")
public class WorkspaceController {


    @GetMapping
    public void getAllWorkspaces() {
        throw new NotImplementedException();
    }
}
