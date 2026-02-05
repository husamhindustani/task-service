-- =============================================================================
-- V1: Create Tasks Table
-- =============================================================================
-- Flyway Migration Naming Convention:
--   V<version>__<description>.sql
--   - V1, V2, V3... (versioned migrations run once in order)
--   - Double underscore separates version from description
--
-- This migration creates the initial tasks table.
-- =============================================================================

CREATE TABLE tasks (
    -- Primary key with auto-increment
    id BIGSERIAL PRIMARY KEY,
    
    -- Task title (required)
    title VARCHAR(255) NOT NULL,
    
    -- Task description (optional, up to 1000 chars)
    description VARCHAR(1000),
    
    -- Status enum stored as string
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraint: status must be one of the valid values
    CONSTRAINT chk_status CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'))
);

-- Index for faster status queries
CREATE INDEX idx_tasks_status ON tasks(status);

-- Index for faster ordering by created_at
CREATE INDEX idx_tasks_created_at ON tasks(created_at DESC);

-- =============================================================================
-- Comments for documentation
-- =============================================================================
COMMENT ON TABLE tasks IS 'Stores task/todo items';
COMMENT ON COLUMN tasks.id IS 'Unique identifier for the task';
COMMENT ON COLUMN tasks.title IS 'Task title/name';
COMMENT ON COLUMN tasks.description IS 'Optional detailed description';
COMMENT ON COLUMN tasks.status IS 'Current status: PENDING, IN_PROGRESS, COMPLETED, CANCELLED';
COMMENT ON COLUMN tasks.created_at IS 'When the task was created';
COMMENT ON COLUMN tasks.updated_at IS 'When the task was last modified';
