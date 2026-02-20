# AI-Assisted Development Log

This document records how AI tools were used during the development of this project.

---

## Tool Used

- ChatGPT (GPT-4)

---

## 1. Improving Robustness of Storage (File I/O Handling)

### Context

The `Storage` class performs file operations such as:

- Creating the data directory and file (`ensureDataFileExists()`)
- Writing task data to file (`saveTasksToFile()`)

These operations interact with the file system and behave like "black-box" operations because they depend on the external environment.

### Explanation

- I used AI for these two functions because they touched on functionalities and libraries I hadn't touched before.
- I specified the end goal and AI suggested me approaches to handle it, such as creating File objects for dir and data when they didn't exist.
- The AI also pointed out several exception handling, such as `saveTasksToFile()` calling `ensureDataFileExists()` as a safeguard.