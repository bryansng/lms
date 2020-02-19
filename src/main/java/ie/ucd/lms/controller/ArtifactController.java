package ie.ucd.lms.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArtifactController {
  @Autowired ArtifactRepository noteRepository;

  int count = 0;
  @GetMapping("/greeting")
  public String greeting(@RequestParam(name="name") String name, Model model) {
    model.addAttribute("name", name);
    model.addAttribute("count", count++);
    return "hello.html";
  }

  @PostMapping("/create")
  public String create(
                  @RequestParam(name="title") String title,
                  @RequestParam(name="note") String note, Model model
                ) {
    Artifact noteObj = note(title, note);
    if (!noteRepository.exists(Example.of(noteObj))) {
      System.out.println("exists");
      noteRepository.save(noteObj);
    }
    printAll();
    return "index.html";
  }
  private Artifact note(String title, String note) {
    Artifact noteObj = new Artifact();
    noteObj.setTitle(title);
    noteObj.setArtifact(note);
    return noteObj;
  }
  private void printAll() {
    for (Artifact note : noteRepository.findAll()) {
      System.out.println(note);
    };
  }

  @GetMapping("/")
  public String indexView() {
    return "index.html";
  }

  @GetMapping("/create")
  public String createArtifactView() {
    return "create.html";
  }

  @GetMapping("/browse")
  public String browseArtifactsView(Model model) {
    model.addAttribute("notes", noteRepository.findAll());
    return "browse.html";
  }

  @GetMapping("/view")
  public String viewArtifact(@RequestParam(name="id") Long id, Model model) {
    Optional<Artifact> opArtifact = noteRepository.findById(id);
    if (opArtifact.isPresent()) {
      model.addAttribute("note", opArtifact.get());
    }
    return "view.html";
  }
}