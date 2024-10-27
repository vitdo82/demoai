-- Create the documents table
CREATE TABLE IF NOT EXISTS documents
(
    id            UUID                     DEFAULT uuid_generate_v4() PRIMARY KEY,
    document_name TEXT UNIQUE NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    metadata      JSONB
);

-- Create the vector_store table with reference to documents
CREATE TABLE IF NOT EXISTS vector_store
(
    id          UUID                     DEFAULT uuid_generate_v4() PRIMARY KEY,
    document_id UUID NOT NULL,
    content     TEXT,
    metadata    JSONB,
    embedding   vector(1024),
    chunk_index INTEGER,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES documents (id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_documents_document_name ON documents (document_name);
CREATE INDEX idx_vector_store_document_id ON vector_store (document_id);
CREATE INDEX idx_vector_store_embedding ON vector_store USING HNSW (embedding vector_cosine_ops);
