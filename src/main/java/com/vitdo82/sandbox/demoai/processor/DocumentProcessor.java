package com.vitdo82.sandbox.demoai.processor;

import org.springframework.core.io.Resource;

public interface DocumentProcessor {

    void processDocument(Resource resource);
}
