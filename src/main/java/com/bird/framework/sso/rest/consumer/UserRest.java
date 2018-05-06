package com.bird.framework.sso.rest.consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("system")
public interface UserRest {
    @RequestMapping("/user/username/{username}")
    String getByUsername(@PathVariable("username") String username);
    @RequestMapping("/user/nick/{nick}")
    String getByNick(@PathVariable("nick") String nick);
    @RequestMapping("/user/mobile/{mobile}")
    String getByMobile(@PathVariable("mobile") String mobile);
}
