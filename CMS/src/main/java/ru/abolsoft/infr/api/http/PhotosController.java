package ru.abolsoft.infr.api.http;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.abolsoft.core.common.exceptions.NotImplementedException;

@RequestMapping("photos")
public class PhotosController {

    @GetMapping
    public void getAllPhotos() {
        throw new NotImplementedException();
    }

    @PostMapping("/search") // think about url name
    public void searchDossier(){
        throw new NotImplementedException();
    }

    @GetMapping("/search") // think about url name
    public void searchPhotos(){
        throw new NotImplementedException();
    }
}
