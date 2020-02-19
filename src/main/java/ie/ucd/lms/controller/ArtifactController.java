package ie.ucd.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArtifactController {
  @Autowired
  ArtifactRepository artifactRepository;

  @GetMapping("/")
  public String indexView() {
    return "index.html";
  }

  private void printAll() {
    for (Artifact artifact : artifactRepository.findAll()) {
      System.out.println(artifact);
    }
    ;
  }
}