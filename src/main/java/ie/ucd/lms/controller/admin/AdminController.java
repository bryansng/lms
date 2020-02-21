package ie.ucd.lms.controller.admin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import ie.ucd.lms.service.admin.ArtifactService;
import ie.ucd.lms.service.admin.Common;
import ie.ucd.lms.service.admin.MemberService;
import ie.ucd.lms.service.admin.LoanHistoryService;
import ie.ucd.lms.service.admin.ReserveQueueService;

@Controller
public class AdminController {
  @Autowired
  MemberService memberService;

  @Autowired
  ArtifactService artifactService;

  @Autowired
  LoanHistoryService loanHistoryService;

  @Autowired
  ReserveQueueService reserveQueueService;

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
    testLostService();
    testLoanService();
    testArtifactService();
    testReserveQueueService();
    System.out.println("\n\nTests completed.\n\n");
  }

  public void testMemberService() {
    Assert.isTrue(memberService.search("bRYaN", 0).size() == 1, " MemberService search() method incorrect.");
    Assert.isTrue(memberService.search("ucd", 0).size() == 4, " MemberService search() method incorrect.");
    Assert.isTrue(memberService.search("1", 0).size() == 2, " MemberService search() method incorrect.");
  }

  public void testArtifactService() {
    Assert.isTrue(artifactService.search("kiyosaki", 0).size() == 1, " ArtifactService search() method incorrect.");
    Assert.isTrue(artifactService.search("", "book", 0).size() == 6,
        " ArtifactService search() method with type incorrect.");

    artifactService.update("1", "9780743269513", "book", "Self-Help", "R. Covey",
        "The 7 Habits of Highly Effective People", "",
        "In The 7 Habits of Highly Effective People, author Stephen R. Covey presents a holistic, integrated, principle-centered approach for solving personal and professional problems. With penetrating insights and pointed anecdotes, Covey reveals a step-by-step pathway for living with fairness, integrity, service, and human dignity—principles that give us the security to adapt to change and the wisdom and power to take advantage of the opportunities that change creates.",
        "Free Press", "2004-11-09", "20.00", "2", "420");
    Assert.isTrue(artifactService.search("", "book", 0).size() == 6, " ArtifactService update() method incorrect.");

    Assert.isTrue(artifactService.delete("3") == true, " ArtifactService delete() method incorrect.");
    Assert.isTrue(artifactService.search("", "book", 0).size() == 5, " ArtifactService delete() method incorrect.");

    artifactService.create("9780062301239", "book", "Biography & Autobiography", "Ashlee Vance", "Elon Musk",
        "Tesla, SpaceX, and the Quest for a Fantastic Future",
        "Elon Musk is the most daring entrepreneur of our time There are few industrialists in history who could match Elon Musk's relentless drive and ingenious vision. A modern alloy of Thomas Edison, Henry Ford, Howard Hughes, and Steve Jobs, Musk is the man behind PayPal, Tesla Motors, SpaceX, and SolarCity, each of which has sent shock waves throughout American business and industry. More than any other executive today, Musk has dedicated his energies and his own vast fortune to inventing a future that is as rich and far-reaching as a science fiction fantasy. In this lively, investigative account, veteran technology journalist Ashlee Vance offers an unprecedented look into the remarkable life and times of Silicon Valley's most audacious businessman. Written with exclusive access to Musk, his family, and his friends, the book traces his journey from his difficult upbringing in South Africa to his ascent to the pinnacle of the global business world. Vance spent more than fifty hours in conversation with Musk and interviewed close to three hundred people to tell the tumultuous stories of Musk's world-changing companies and to paint a portrait of a complex man who has renewed American industry and sparked new levels of innovation—all while making plenty of enemies along the way. In 1992, Elon Musk arrived in the United States as a ferociously driven immigrant bent on realizing his wildest dreams. Since then, Musk's roller-coaster life has brought him grave disappointments alongside massive successes. After being forced out of PayPal, fending off a life-threatening case of malaria, and dealing with the death of his infant son, Musk abandoned Silicon Valley for Los Angeles. He spent the next few years baffling his friends by blowing his entire fortune on rocket ships and electric cars. Cut to 2012, however, and Musk had mounted one of the greatest resurrections in business history: Tesla, SpaceX, and SolarCity had enjoyed unparalleled success, and Musk's net worth soared to more than $5 billion. At a time when many American companies are more interested in chasing easy money than in taking bold risks on radical new technology, Musk stands out as the only businessman with enough dynamism and vision to tackle—and even revolutionize—three industries at once. Vance makes the case that Musk's success heralds a return to the original ambition and invention that made America an economic and intellectual powerhouse. Elon Musk is a brilliant, penetrating examination of what Musk's career means for a technology industry undergoing dramatic change and offers a taste of what could be an incredible century ahead.",
        "Ecco", "2015-05-19", "10.00", "3", "421");
    Assert.isTrue(artifactService.search("", "book", 0).size() == 6, " ArtifactService create() method incorrect.");
  }

  public void testLoanService() {
    String nowDate = LocalDateTime.now().format(Common.dateFormatter);
    String nowPlus3Date = LocalDateTime.now().plusDays(3).format(Common.dateFormatter);

    Assert.isTrue(loanHistoryService.searchAllButLost("", "", 0).size() == 4,
        " loanHistoryService search() method incorrect.");
    Assert.isTrue(loanHistoryService.searchAllButLost(nowDate, "", 0).size() == 4,
        " loanHistoryService search() method incorrect.");
    Assert.isTrue(loanHistoryService.searchAllButLost("", nowDate, 0).size() == 4,
        " loanHistoryService search() method incorrect.");
    Assert.isTrue(loanHistoryService.searchAllButLost(nowDate, nowDate, 0).size() == 4,
        " loanHistoryService search() method incorrect.");

    Assert.isTrue(loanHistoryService.searchAllButLost("", "", "issued date", 0).size() == 4,
        " loanHistoryService search() method with type incorrect.");
    Assert.isTrue(loanHistoryService.searchAllButLost(nowDate, "", "issued date", 0).size() == 4,
        " loanHistoryService search() method with type incorrect.");
    Assert.isTrue(loanHistoryService.searchAllButLost("", nowDate, "issued date", 0).size() == 4,
        " loanHistoryService search() method with type incorrect.");
    Assert.isTrue(loanHistoryService.searchAllButLost(nowDate, nowDate, "issued date", 0).size() == 4,
        " loanHistoryService search() method with type incorrect.");

    loanHistoryService.update("3", "9780743269513", "3", nowPlus3Date, "0.0", "issued");
    Assert.isTrue(loanHistoryService.searchAllButLost("", "", "issued date", 0).size() == 4,
        " loanHistoryService update() method incorrect.");

    Assert.isTrue(loanHistoryService.delete("4") == true, " loanHistoryService delete() method incorrect.");
    Assert.isTrue(loanHistoryService.searchAllButLost("", "", 0).size() == 3,
        " loanHistoryService delete() method incorrect.");

    loanHistoryService.create("9780751532715", "3", nowPlus3Date, "0.0", "issued");
    Assert.isTrue(loanHistoryService.searchAllButLost("", "", "issued date", 0).size() == 4,
        " loanHistoryService create() method incorrect.");
  }

  public void testLostService() {
    String nowDate = LocalDateTime.now().format(Common.dateFormatter);
    String nowPlus3Date = LocalDateTime.now().plusDays(3).format(Common.dateFormatter);

    loanHistoryService.create("9780743269513", "4", nowPlus3Date, "0.0", "lost");
    Assert.isTrue(loanHistoryService.searchLost("", "", 0).size() == 1,
        " loanHistoryService create() method incorrect.");

    Assert.isTrue(loanHistoryService.searchLost("", "", 0).size() == 1,
        " loanHistoryService search() method incorrect.");
    Assert.isTrue(loanHistoryService.searchLost(nowDate, "", 0).size() == 1,
        " loanHistoryService search() method incorrect.");
    Assert.isTrue(loanHistoryService.searchLost("", nowDate, 0).size() == 1,
        " loanHistoryService search() method incorrect.");
    Assert.isTrue(loanHistoryService.searchLost(nowDate, nowDate, 0).size() == 1,
        " loanHistoryService search() method incorrect.");

    loanHistoryService.update("6", "9780743269513", "3", nowPlus3Date, "0.0", "lost");
    Assert.isTrue(loanHistoryService.searchLost("", "", 0).size() == 1,
        " loanHistoryService update() method incorrect.");

    Assert.isTrue(loanHistoryService.delete("5") == true, " loanHistoryService delete() method incorrect.");
    Assert.isTrue(loanHistoryService.searchLost("", "", 0).size() == 0,
        " loanHistoryService delete() method incorrect.");
  }

  public void testReserveQueueService() {
    String nowDate = LocalDateTime.now().format(Common.dateFormatter);
    String nowPlus3Date = LocalDateTime.now().plusDays(3).format(Common.dateFormatter);

    Assert.isTrue(reserveQueueService.search("", "", 0).size() == 5, " reserveQueueService search() method incorrect.");
    Assert.isTrue(reserveQueueService.search(nowPlus3Date, "", 0).size() == 5,
        " reserveQueueService search() method incorrect.");
    reserveQueueService.search("", nowPlus3Date, 0);
    Assert.isTrue(reserveQueueService.search("", nowPlus3Date, 0).size() == 5,
        " reserveQueueService search() method incorrect.");
    Assert.isTrue(reserveQueueService.search(nowPlus3Date, nowPlus3Date, 0).size() == 5,
        " reserveQueueService search() method incorrect.");

    reserveQueueService.update("4", "9780743269513", "4", nowPlus3Date);
    Assert.isTrue(reserveQueueService.search("", "", 0).size() == 5, " reserveQueueService update() method incorrect.");

    Assert.isTrue(reserveQueueService.delete("5") == true, " reserveQueueService delete() method incorrect.");
    Assert.isTrue(reserveQueueService.search("", "", 0).size() == 4, " reserveQueueService delete() method incorrect.");

    reserveQueueService.create("9780751532715", "3", nowPlus3Date);
    Assert.isTrue(reserveQueueService.search("", "", 0).size() == 5, " reserveQueueService create() method incorrect.");
  }
}