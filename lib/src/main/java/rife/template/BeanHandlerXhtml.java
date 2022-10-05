/*
 * Copyright 2001-2022 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 */
package rife.template;

import rife.tools.BeanUtils;
import rife.tools.exceptions.BeanUtilsException;

import java.util.Map;

public class BeanHandlerXhtml extends AbstractBeanHandler {
    // TODO
//    private FormBuilder mFormBuilder = new FormBuilderXhtml();

    BeanHandlerXhtml() {
    }

    public static BeanHandlerXhtml getInstance() {
        return BeanHandlerXhtmlSingleton.INSTANCE;
    }

    // TODO
//    public MimeType getMimeType() {
//        return MimeType.APPLICATION_XHTML;
//    }
//
//    public FormBuilder getFormBuilder() {
//        return mFormBuilder;
//    }

    protected Map<String, Object> getPropertyValues(Template template, Object bean, String prefix)
    throws BeanUtilsException {
        return BeanUtils.getPropertyValues(BeanUtils.Accessors.GETTERS, bean, null, null, prefix);
    }
}

