package org.example.hogwarts.controller;

import org.example.hogwarts.model.Avatar;
import org.example.hogwarts.repository.AvatarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/avatars")
public class AvatarTController {

    private final AvatarRepository avatarRepository;

    public AvatarTController(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    @GetMapping
    public Page<Avatar> getAvatarsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return avatarRepository.findAll(PageRequest.of(page, size));
    }
}
