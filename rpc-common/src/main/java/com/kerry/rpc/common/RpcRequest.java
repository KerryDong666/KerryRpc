package com.kerry.rpc.common;

/**
 * 封装 RPC 请求封装发送的object的反射属性
 * @author Kerry Dong
 */
public class RpcRequest {
    /**请求id.*/
    private String requestId;
    /**类名称.*/
    private String className;
    /**方法名称.*/
    private String methodName;
    /**方法类型.*/
    private Class<?>[] parameterTypes;
    /**方法参数.*/
    private Object[] parameters;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
