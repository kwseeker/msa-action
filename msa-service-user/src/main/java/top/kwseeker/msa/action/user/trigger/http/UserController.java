package top.kwseeker.msa.action.user.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.kwseeker.msa.action.framework.common.model.Response;
import top.kwseeker.msa.action.user.domain.user.model.entity.UserEntity;
import top.kwseeker.msa.action.user.domain.user.service.IUserService;
import top.kwseeker.msa.action.user.trigger.http.dto.UserCreateDTO;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/manage")
public class UserController {

    @Resource
    private IUserService userService;

    @GetMapping("/get")
    public Response<UserEntity> getUser(@RequestParam("uid") Long uid) {
        UserEntity userEntity = userService.getUser(uid);
        return Response.success(userEntity);
    }

    @GetMapping("/getByName")
    public Response<UserEntity> getUserByUsername(@RequestParam("username") String username) {
        UserEntity userEntity = userService.getUserByUsername(username);
        return Response.success(userEntity);
    }

    @PutMapping("/create")
    public Response<Long> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        Long userId = userService.createUser(userCreateDTO);
        return Response.success(userId);
    }
}
