# Makefile for Jenkins Plugin Build
.PHONY: package clean test compile install version shell help

# Docker image with Maven and JDK 21
DOCKER_IMAGE := maven:3.9.9-eclipse-temurin-21
PROJECT_DIR := $(PWD)
MAVEN_CACHE := $(HOME)/.m2
USER_ID := $(shell id -u)
GROUP_ID := $(shell id -g)
DOCKER_RUN := docker run --rm --entrypoint="" -u $(USER_ID):$(GROUP_ID) -v $(PWD):$(PROJECT_DIR) -v $(MAVEN_CACHE):/home/ubuntu/.m2 -w $(PROJECT_DIR) $(DOCKER_IMAGE)

# Default target
.DEFAULT_GOAL := help

# Build the plugin package
package:
	@echo "Building Jenkins plugin package using Docker..."
	@$(DOCKER_RUN) mvn clean package

# Clean build artifacts
clean:
	@echo "Cleaning build artifacts..."
	@$(DOCKER_RUN) mvn clean

# Run tests
test:
	@echo "Running tests..."
	@$(DOCKER_RUN) mvn test

# Compile only (faster for development)
compile:
	@echo "Compiling sources..."
	@$(DOCKER_RUN) mvn compile

# Install dependencies and compile
install:
	@echo "Installing dependencies and compiling..."
	@$(DOCKER_RUN) mvn install

# Show Maven and Java versions
version:
	@echo "Maven and Java versions in container:"
	@$(DOCKER_RUN) mvn --version

# Interactive shell in container
shell:
	@echo "Opening interactive shell in Maven container..."
	@docker run --rm -it --entrypoint="" -u $(USER_ID):$(GROUP_ID) -v $(PWD):$(PROJECT_DIR) -v $(MAVEN_CACHE):/home/ubuntu/.m2 -w $(PROJECT_DIR) $(DOCKER_IMAGE) /bin/bash

# Show help
help:
	@echo "Jenkins Plugin Build Makefile"
	@echo ""
	@echo "Available targets:"
	@echo "  help       - Show this help message"
	@echo "  package    - Build the plugin package (mvn clean package)"
	@echo "  clean      - Clean build artifacts (mvn clean)"
	@echo "  test       - Run tests (mvn test)"
	@echo "  compile    - Compile sources only (mvn compile)"
	@echo "  install    - Install dependencies and compile (mvn install)"
	@echo "  version    - Show Maven and Java versions"
	@echo "  shell      - Open interactive shell in container"
