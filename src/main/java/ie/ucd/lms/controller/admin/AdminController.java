package ie.ucd.lms.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import ie.ucd.lms.service.admin.ArtifactService;
import ie.ucd.lms.service.admin.MemberService;

@Controller
public class AdminController {
  @Autowired
  MemberService memberService;

  @Autowired
  ArtifactService artifactService;

  @GetMapping("/admin/dashboard")
  public String indexView() {
    return "admin/index.html";
  }

  @GetMapping("/admin/tests")
  public String testIndexView() {
    tests();
    return "admin/index.html";
  }

  public void tests() {
    testMemberService();
    testArtifactService();
  }

  public void testMemberService() {
    Assert.isTrue(memberService.search("bRYaN", 0).size() == 1, " MemberService search() method incorrect.");
    Assert.isTrue(memberService.search("ucd", 0).size() == 3, " MemberService search() method incorrect.");
    Assert.isTrue(memberService.search("1", 0).size() == 2, " MemberService search() method incorrect.");
  }

  public void testArtifactService() {
    Assert.isTrue(artifactService.search("kiyosaki", 0).size() == 1, " ArtifactService search() method incorrect.");
    Assert.isTrue(artifactService.search("", "book", 0).size() == 3,
        " ArtifactService search() method with type incorrect.");

    artifactService.update("1", "9780743269513", "book", "Self-Help", "R. Covey",
        "The 7 Habits of Highly Effective People", "",
        "In The 7 Habits of Highly Effective People, author Stephen R. Covey presents a holistic, integrated, principle-centered approach for solving personal and professional problems. With penetrating insights and pointed anecdotes, Covey reveals a step-by-step pathway for living with fairness, integrity, service, and human dignity—principles that give us the security to adapt to change and the wisdom and power to take advantage of the opportunities that change creates.",
        "Free Press", "2004-11-09", "20.00", "2", "420");
    Assert.isTrue(artifactService.search("", "book", 0).size() == 3, " ArtifactService update() method incorrect.");

    Assert.isTrue(artifactService.delete("3") == true, " ArtifactService delete() method incorrect.");
    Assert.isTrue(artifactService.search("", "book", 0).size() == 2, " ArtifactService delete() method incorrect.");

    artifactService.create("9780062301239", "book", "Biography & Autobiography", "Ashlee Vance", "Elon Musk",
        "Tesla, SpaceX, and the Quest for a Fantastic Future",
        "Elon Musk is the most daring entrepreneur of our time There are few industrialists in history who could match Elon Musk's relentless drive and ingenious vision. A modern alloy of Thomas Edison, Henry Ford, Howard Hughes, and Steve Jobs, Musk is the man behind PayPal, Tesla Motors, SpaceX, and SolarCity, each of which has sent shock waves throughout American business and industry. More than any other executive today, Musk has dedicated his energies and his own vast fortune to inventing a future that is as rich and far-reaching as a science fiction fantasy. In this lively, investigative account, veteran technology journalist Ashlee Vance offers an unprecedented look into the remarkable life and times of Silicon Valley's most audacious businessman. Written with exclusive access to Musk, his family, and his friends, the book traces his journey from his difficult upbringing in South Africa to his ascent to the pinnacle of the global business world. Vance spent more than fifty hours in conversation with Musk and interviewed close to three hundred people to tell the tumultuous stories of Musk's world-changing companies and to paint a portrait of a complex man who has renewed American industry and sparked new levels of innovation—all while making plenty of enemies along the way. In 1992, Elon Musk arrived in the United States as a ferociously driven immigrant bent on realizing his wildest dreams. Since then, Musk's roller-coaster life has brought him grave disappointments alongside massive successes. After being forced out of PayPal, fending off a life-threatening case of malaria, and dealing with the death of his infant son, Musk abandoned Silicon Valley for Los Angeles. He spent the next few years baffling his friends by blowing his entire fortune on rocket ships and electric cars. Cut to 2012, however, and Musk had mounted one of the greatest resurrections in business history: Tesla, SpaceX, and SolarCity had enjoyed unparalleled success, and Musk's net worth soared to more than $5 billion. At a time when many American companies are more interested in chasing easy money than in taking bold risks on radical new technology, Musk stands out as the only businessman with enough dynamism and vision to tackle—and even revolutionize—three industries at once. Vance makes the case that Musk's success heralds a return to the original ambition and invention that made America an economic and intellectual powerhouse. Elon Musk is a brilliant, penetrating examination of what Musk's career means for a technology industry undergoing dramatic change and offers a taste of what could be an incredible century ahead.",
        "Ecco", "2015-05-19", "10.00", "3", "421");
    Assert.isTrue(artifactService.search("", "book", 0).size() == 3, " ArtifactService create() method incorrect.");
  }
}