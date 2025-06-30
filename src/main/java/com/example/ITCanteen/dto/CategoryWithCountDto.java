package com.example.ITCanteen.dto;

public class CategoryWithCountDto {
    private Long id;
    private String name;
    private String description;
    private int count;

    public CategoryWithCountDto(Long id, String name, String description, int count) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.count = count;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCount() { return count; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCount(int count) { this.count = count; }
} 