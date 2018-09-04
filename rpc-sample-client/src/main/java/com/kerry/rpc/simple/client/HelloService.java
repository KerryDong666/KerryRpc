package com.kerry.rpc.simple.client;

import javax.annotation.Resource;

@Resource
public interface HelloService {

    String hello(String name);

    String hello(Person person);
}
