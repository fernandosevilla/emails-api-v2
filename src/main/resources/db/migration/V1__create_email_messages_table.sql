CREATE TABLE email_messages (
    id UUID PRIMARY KEY,
    recipient VARCHAR(320) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    sent_at TIMESTAMP NULL,
    failed_at TIMESTAMP NULL,
    failure_reason TEXT NULL
);

CREATE INDEX idx_email_messages_status ON email_messages(status);
CREATE INDEX idx_email_messages_created_at ON email_messages(created_at);
