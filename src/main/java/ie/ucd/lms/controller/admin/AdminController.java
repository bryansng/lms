package ie.ucd.lms.controller.admin;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import ie.ucd.lms.service.admin.ArtifactService;
import ie.ucd.lms.service.admin.Common;
import ie.ucd.lms.service.admin.MemberService;
import ie.ucd.lms.service.admin.LoanHistoryService;
import ie.ucd.lms.service.admin.ReserveQueueService;
import ie.ucd.lms.service.admin.ReportService;

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

	@Autowired
	ReportService reportService;

	@GetMapping("/admin/dashboard")
	public String dashboardView(Model model) {
		model.addAttribute("todayFine", reportService.fine(reportService.today));
		model.addAttribute("monthFine", reportService.fine(reportService.thisMonth));
		model.addAttribute("yearFine", reportService.fine(reportService.thisYear));
		model.addAttribute("todayArtifacts", reportService.totalArtifacts(reportService.today));
		model.addAttribute("monthArtifacts", reportService.totalArtifacts(reportService.thisMonth));
		model.addAttribute("yearArtifacts", reportService.totalArtifacts(reportService.thisYear));
		model.addAttribute("todayIssued", reportService.artifactsIssued(reportService.today));
		model.addAttribute("monthIssued", reportService.artifactsIssued(reportService.thisMonth));
		model.addAttribute("yearIssued", reportService.artifactsIssued(reportService.thisYear));
		model.addAttribute("todayReturned", reportService.artifactsReturned(reportService.today));
		model.addAttribute("monthReturned", reportService.artifactsReturned(reportService.thisMonth));
		model.addAttribute("yearReturned", reportService.artifactsReturned(reportService.thisYear));
		model.addAttribute("todayLost", reportService.artifactsLost(reportService.today));
		model.addAttribute("monthLost", reportService.artifactsLost(reportService.thisMonth));
		model.addAttribute("yearLost", reportService.artifactsLost(reportService.thisYear));
		return "admin/index.html";
	}

	@GetMapping("/admin/members/view")
	public String membersView(@RequestParam(defaultValue = "0", required = false) Integer page, Model model) {
		model.addAttribute("totalRows", Common.PAGINATION_ROWS);
		model.addAttribute("members", memberService.search("", page));
		return "admin/memberView.html";
	}

	@GetMapping("/admin/artifacts/view")
	public String artifactsView(Model model) {
		return "admin/artifactView.html";
	}

	@GetMapping("/admin/loans/view")
	public String loansView(Model model) {
		return "admin/loanView.html";
	}

	@GetMapping("/admin/losts/view")
	public String lostsView(Model model) {
		return "admin/lostView.html";
	}

	@GetMapping("/admin/reserves/view")
	public String reservesView(Model model) {
		return "admin/reserveView.html";
	}

	@GetMapping("/admin/tests")
	public String testIndexView() {
		tests();
		return "admin/index.html";
	}

	public void tests() {
		// testRelationship();
		testMemberService();
		testLostService();
		testLoanService();
		testArtifactService();
		testReserveQueueService();
		testLoanFunctionInReserveQueueService();
		testRestockedFunctionInLoanHistoryService();
		testReturnnFunctionInLoanHistoryService();
		testRenewFunctionInLoanHistoryService();
		testLostFunctionInLoanHistoryService();
		testOnCreateWillAddToReserveQueue();
		testNextInLineFunctionInReserveQueueService();
		testReportService();
		System.out.println("\n\nTests completed.\n\n");
	}

	public void testRelationship() {
		// loanHistoryService.printAll();
		// artifactService.printAll();
		// artifactService.printAllWithReserveQueue();
		memberService.printAllWithLoanHistory();
		memberService.printAllWithReserveQueue();
	}

	public void testMemberService() {
		Assert.isTrue(memberService.search("bRYaN", 0).getTotalElements() == 1,
				" MemberService search() method incorrect.");
		Assert.isTrue(memberService.search("ucd", 0).getTotalElements() == 4, " MemberService search() method incorrect.");
		Assert.isTrue(memberService.search("1", 0).getTotalElements() == 2, " MemberService search() method incorrect.");
	}

	public void testArtifactService() {
		Assert.isTrue(artifactService.search("kiyosaki", 0).getTotalElements() == 1,
				" ArtifactService search() method incorrect.");
		Assert.isTrue(artifactService.search("", "book", 0).getTotalElements() == 6,
				" ArtifactService search() method with type incorrect.");

		artifactService.update("1", "9780743269513", "book", "Self-Help", "R. Covey",
				"The 7 Habits of Highly Effective People", "",
				"In The 7 Habits of Highly Effective People, author Stephen R. Covey presents a holistic, integrated, principle-centered approach for solving personal and professional problems. With penetrating insights and pointed anecdotes, Covey reveals a step-by-step pathway for living with fairness, integrity, service, and human dignity—principles that give us the security to adapt to change and the wisdom and power to take advantage of the opportunities that change creates.",
				"Free Press", "2004-11-09", "30.00", "2", "2", "420");
		Assert.isTrue(artifactService.search("", "book", 0).getTotalElements() == 6,
				" ArtifactService update() method incorrect.");

		Assert.isTrue(artifactService.delete("3") == true, " ArtifactService delete() method incorrect.");
		Assert.isTrue(artifactService.search("", "book", 0).getTotalElements() == 5,
				" ArtifactService delete() method incorrect.");

		artifactService.create("9780062301239", "book", "Biography & Autobiography", "Ashlee Vance", "Elon Musk",
				"Tesla, SpaceX, and the Quest for a Fantastic Future",
				"Elon Musk is the most daring entrepreneur of our time There are few industrialists in history who could match Elon Musk's relentless drive and ingenious vision. A modern alloy of Thomas Edison, Henry Ford, Howard Hughes, and Steve Jobs, Musk is the man behind PayPal, Tesla Motors, SpaceX, and SolarCity, each of which has sent shock waves throughout American business and industry. More than any other executive today, Musk has dedicated his energies and his own vast fortune to inventing a future that is as rich and far-reaching as a science fiction fantasy. In this lively, investigative account, veteran technology journalist Ashlee Vance offers an unprecedented look into the remarkable life and times of Silicon Valley's most audacious businessman. Written with exclusive access to Musk, his family, and his friends, the book traces his journey from his difficult upbringing in South Africa to his ascent to the pinnacle of the global business world. Vance spent more than fifty hours in conversation with Musk and interviewed close to three hundred people to tell the tumultuous stories of Musk's world-changing companies and to paint a portrait of a complex man who has renewed American industry and sparked new levels of innovation—all while making plenty of enemies along the way. In 1992, Elon Musk arrived in the United States as a ferociously driven immigrant bent on realizing his wildest dreams. Since then, Musk's roller-coaster life has brought him grave disappointments alongside massive successes. After being forced out of PayPal, fending off a life-threatening case of malaria, and dealing with the death of his infant son, Musk abandoned Silicon Valley for Los Angeles. He spent the next few years baffling his friends by blowing his entire fortune on rocket ships and electric cars. Cut to 2012, however, and Musk had mounted one of the greatest resurrections in business history: Tesla, SpaceX, and SolarCity had enjoyed unparalleled success, and Musk's net worth soared to more than $5 billion. At a time when many American companies are more interested in chasing easy money than in taking bold risks on radical new technology, Musk stands out as the only businessman with enough dynamism and vision to tackle—and even revolutionize—three industries at once. Vance makes the case that Musk's success heralds a return to the original ambition and invention that made America an economic and intellectual powerhouse. Elon Musk is a brilliant, penetrating examination of what Musk's career means for a technology industry undergoing dramatic change and offers a taste of what could be an incredible century ahead.",
				"Ecco", "2015-05-19", "10.00", "0", "0", "421");
		Assert.isTrue(artifactService.search("", "book", 0).getTotalElements() == 6,
				" ArtifactService create() method incorrect.");
	}

	public void testLoanService() {
		Assert.isTrue(loanHistoryService.searchAllButLost("", "", 0).getTotalElements() == 4,
				" loanHistoryService search() method incorrect.");
		Assert.isTrue(loanHistoryService.searchAllButLost(Common.nowDate, "", 0).getTotalElements() == 4,
				" loanHistoryService search() method incorrect.");
		Assert.isTrue(loanHistoryService.searchAllButLost("", Common.nowDate, 0).getTotalElements() == 4,
				" loanHistoryService search() method incorrect.");
		Assert.isTrue(loanHistoryService.searchAllButLost(Common.nowDate, Common.nowDate, 0).getTotalElements() == 4,
				" loanHistoryService search() method incorrect.");

		Assert.isTrue(loanHistoryService.searchAllButLost("", "", "issued date", 0).getTotalElements() == 4,
				" loanHistoryService search() method with type incorrect.");
		Assert.isTrue(loanHistoryService.searchAllButLost(Common.nowDate, "", "issued date", 0).getTotalElements() == 4,
				" loanHistoryService search() method with type incorrect.");
		Assert.isTrue(loanHistoryService.searchAllButLost("", Common.nowDate, "issued date", 0).getTotalElements() == 4,
				" loanHistoryService search() method with type incorrect.");
		Assert.isTrue(
				loanHistoryService.searchAllButLost(Common.nowDate, Common.nowDate, "issued date", 0).getTotalElements() == 4,
				" loanHistoryService search() method with type incorrect.");

		loanHistoryService.update("3", "9780743269513", "3", Common.nowPlus3Date, "0.0", "issued");
		Assert.isTrue(loanHistoryService.searchAllButLost("", "", "issued date", 0).getTotalElements() == 4,
				" loanHistoryService update() method incorrect.");

		Assert.isTrue(loanHistoryService.delete("4") == true, " loanHistoryService delete() method incorrect.");
		Assert.isTrue(loanHistoryService.searchAllButLost("", "", 0).getTotalElements() == 3,
				" loanHistoryService delete() method incorrect.");

		loanHistoryService.create("9780751532715", "3", Common.nowPlus3Date, "0.0", "issued");
		Assert.isTrue(loanHistoryService.searchAllButLost("", "", "issued date", 0).getTotalElements() == 4,
				" loanHistoryService create() method incorrect.");
	}

	public void testLostService() {
		loanHistoryService.create("9780743269513", "4", Common.nowPlus3Date, "0.0", "lost");
		Assert.isTrue(loanHistoryService.searchLost("", "", 0).getTotalElements() == 1,
				" loanHistoryService create() method incorrect.");

		Assert.isTrue(loanHistoryService.searchLost("", "", 0).getTotalElements() == 1,
				" loanHistoryService search() method incorrect.");
		Assert.isTrue(loanHistoryService.searchLost(Common.nowDate, "", 0).getTotalElements() == 1,
				" loanHistoryService search() method incorrect.");
		Assert.isTrue(loanHistoryService.searchLost("", Common.nowDate, 0).getTotalElements() == 1,
				" loanHistoryService search() method incorrect.");
		Assert.isTrue(loanHistoryService.searchLost(Common.nowDate, Common.nowDate, 0).getTotalElements() == 1,
				" loanHistoryService search() method incorrect.");

		loanHistoryService.update("6", "9780743269513", "3", Common.nowPlus3Date, "0.0", "lost");
		Assert.isTrue(loanHistoryService.searchLost("", "", 0).getTotalElements() == 1,
				" loanHistoryService update() method incorrect.");

		Assert.isTrue(loanHistoryService.delete("5") == true, " loanHistoryService delete() method incorrect.");
		Assert.isTrue(loanHistoryService.searchLost("", "", 0).getTotalElements() == 0,
				" loanHistoryService delete() method incorrect.");
	}

	public void testReserveQueueService() {
		Assert.isTrue(reserveQueueService.search("", "", 0).getTotalElements() == 5,
				" reserveQueueService search() method incorrect.");
		Assert.isTrue(reserveQueueService.search(Common.nowPlus3Date, "", 0).getTotalElements() == 5,
				" reserveQueueService search() method incorrect.");
		reserveQueueService.search("", Common.nowPlus3Date, 0);
		Assert.isTrue(reserveQueueService.search("", Common.nowPlus3Date, 0).getTotalElements() == 5,
				" reserveQueueService search() method incorrect.");
		Assert.isTrue(reserveQueueService.search(Common.nowPlus3Date, Common.nowPlus3Date, 0).getTotalElements() == 5,
				" reserveQueueService search() method incorrect.");

		reserveQueueService.update("4", "9780743269513", "4", Common.nowPlus3Date);
		Assert.isTrue(reserveQueueService.search("", "", 0).getTotalElements() == 5,
				" reserveQueueService update() method incorrect.");

		Assert.isTrue(reserveQueueService.delete("5") == true, " reserveQueueService delete() method incorrect.");
		Assert.isTrue(reserveQueueService.search("", "", 0).getTotalElements() == 4,
				" reserveQueueService delete() method incorrect.");

		reserveQueueService.create("9780751532715", "4", Common.nowPlus3Date);
		Assert.isTrue(reserveQueueService.search("", "", 0).getTotalElements() == 5,
				" reserveQueueService create() method incorrect.");
	}

	public void testLoanFunctionInReserveQueueService() {
		reserveQueueService.loan("6", "7");
		Assert.isTrue(reserveQueueService.search("", "", 0).getTotalElements() == 4,
				" reserveQueueService loan() method incorrect.");
		Assert.isTrue(loanHistoryService.searchAllButLost("", "", "issued date", 0).getTotalElements() == 5,
				" reserveQueueService loan() method incorrect.");
	}

	public void testRestockedFunctionInLoanHistoryService() {
		loanHistoryService.create("9780743269513", "4", Common.nowPlus3Date, "0.0", "lost");
		Assert.isTrue(loanHistoryService.searchLost("", "", 0).getTotalElements() == 1,
				" loanHistoryService restocked() method incorrect.");
		Assert.isTrue(loanHistoryService.searchAllButLost("", "", 0).getTotalElements() == 5,
				" loanHistoryService restocked() method incorrect.");

		loanHistoryService.restocked("8");
		Assert.isTrue(loanHistoryService.searchLost("", "", 0).getTotalElements() == 0,
				" loanHistoryService restocked() method incorrect.");
		Assert.isTrue(loanHistoryService.searchAllButLost("", "", 0).getTotalElements() == 6,
				" loanHistoryService restocked() method incorrect.");
	}

	public void testReturnnFunctionInLoanHistoryService() {
		loanHistoryService.returnn("7");
		Assert.isTrue(loanHistoryService.searchAll("", "", "returned", 0).getTotalElements() == 1,
				" loanHistoryService returnn() method incorrect.");
		Assert.isTrue(loanHistoryService.searchAllButLost("", "", 0).getTotalElements() == 6,
				" loanHistoryService returnn() method incorrect.");
	}

	public void testRenewFunctionInLoanHistoryService() {
		loanHistoryService.renew("6", "3");
		Assert.isTrue(loanHistoryService.searchAll("", "", "renewed", 0).getTotalElements() == 1,
				" loanHistoryService renew() method incorrect.");
		Assert.isTrue(loanHistoryService.searchAllButLost("", "", 0).getTotalElements() == 6,
				" loanHistoryService renew() method incorrect.");
	}

	public void testLostFunctionInLoanHistoryService() {
		loanHistoryService.lost("3");
		Assert.isTrue(loanHistoryService.searchAll("", "", "lost", 0).getTotalElements() == 1,
				" loanHistoryService lost() method incorrect.");
		Assert.isTrue(loanHistoryService.searchAllButLost("", "", 0).getTotalElements() == 5,
				" loanHistoryService lost() method incorrect.");
	}

	public void testOnCreateWillAddToReserveQueue() {
		loanHistoryService.create("9780062301239", "3", Common.nowPlus3Date, "0.0", "issued");
		Assert.isTrue(loanHistoryService.searchAll("", "", "issued", 0).getTotalElements() == 2,
				" loanHistoryService create() method incorrect.");
		Assert.isTrue(reserveQueueService.search("", "", 0).getTotalElements() == 5,
				" loanHistoryService create() method incorrect.");
	}

	public void testNextInLineFunctionInReserveQueueService() {
		loanHistoryService.create("9780062301239", "3", Common.nowPlus3Date, "0.0", "issued");
		Assert.isTrue(loanHistoryService.searchAll("", "", "issued", 0).getTotalElements() == 2,
				" loanHistoryService create() method incorrect.");
		Assert.isTrue(reserveQueueService.search("", "", 0).getTotalElements() == 5,
				" loanHistoryService create() method incorrect.");
	}

	public void testReportService() {
		Assert.isTrue(reportService.fine(reportService.today).equals(new BigDecimal("30.00")),
				" reportService fine() method for today incorrect.");
		Assert.isTrue(reportService.fine(reportService.thisMonth).equals(new BigDecimal("30.00")),
				" reportService fine() method for this month incorrect.");
		Assert.isTrue(reportService.fine(reportService.thisYear).equals(new BigDecimal("30.00")),
				" reportService fine() method for this year incorrect.");

		Assert.isTrue(reportService.totalArtifacts(reportService.today) == 6,
				" reportService totalArtifacts() method for today incorrect.");
		Assert.isTrue(reportService.totalArtifacts(reportService.thisMonth) == 6,
				" reportService totalArtifacts() method for this month incorrect.");
		Assert.isTrue(reportService.totalArtifacts(reportService.thisYear) == 6,
				" reportService totalArtifacts() method for this year incorrect.");

		Assert.isTrue(reportService.artifactsIssued(reportService.today) == 6,
				" reportService artifactsIssued() method for today incorrect.");
		Assert.isTrue(reportService.artifactsIssued(reportService.thisMonth) == 6,
				" reportService artifactsIssued() method for this month incorrect.");
		Assert.isTrue(reportService.artifactsIssued(reportService.thisYear) == 6,
				" reportService artifactsIssued() method for this year incorrect.");

		Assert.isTrue(reportService.artifactsReturned(reportService.today) == 1,
				" reportService artifactsReturned() method for today incorrect.");
		Assert.isTrue(reportService.artifactsReturned(reportService.thisMonth) == 1,
				" reportService artifactsReturned() method for this month incorrect.");
		Assert.isTrue(reportService.artifactsReturned(reportService.thisYear) == 1,
				" reportService artifactsReturned() method for this year incorrect.");

		Assert.isTrue(reportService.artifactsLost(reportService.today) == 1,
				" reportService artifactsLost() method for today incorrect.");
		Assert.isTrue(reportService.artifactsLost(reportService.thisMonth) == 1,
				" reportService artifactsLost() method for this month incorrect.");
		Assert.isTrue(reportService.artifactsLost(reportService.thisYear) == 1,
				" reportService artifactsLost() method for this year incorrect.");
	}
}