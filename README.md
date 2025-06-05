# Shticell - Spreadsheet Application

A modern spreadsheet application with a graphical user interface (JavaFX) and advanced features for data management, analysis, and collaboration.

---

## Features

| Feature                | Description                                                      |
|------------------------|------------------------------------------------------------------|
| Real-time calculations | Automatic updates for formulas and cell dependencies             |
| Sorting & Filtering    | Advanced tools for organizing and viewing your data              |
| Version control        | Track changes and manage multiple versions of your sheets        |
| Multi-user collaboration | Work together on the same sheet in real time                  |
| What-If analysis       | Dynamic scenario analysis with adjustable parameters             |
| Custom cell styling    | Change cell colors, text, and styles easily                     |
| Graphs & Charts        | Visualize your data with built-in graphing tools                |
| Range management       | Define and manage named ranges for quick access                 |

---

## Code Structure

```
engine/           # Core spreadsheet logic and data model
WebApp/           # Web application components (if applicable)
ShticellClient/   # Client-side logic and networking
DTO/              # Data Transfer Objects for communication between layers

# Previous versions (for reference only):
# GUI/ - Old JavaFX graphical version
# UI/  - Old console version
```

---

## Usage Example

- **Edit a cell:** Click a cell, enter a new value, and see all dependent cells update instantly.
- **Apply What-If:** Use the What-If slider to simulate changes and analyze results in real time.
- **Sort/Filter:** Select a range and sort or filter your data by any column.
- **Create a graph:** Select data ranges and generate a graph with one click.

---

