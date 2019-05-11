package it.polito.tdp.poweroutages.model;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.poweroutages.db.PowerOutageDAO;

public class Model {

	PowerOutageDAO podao;
	List<Nerc> nerc;
	List<PowOutages> res;

	public Model() {
		podao = new PowerOutageDAO();
		nerc = new ArrayList<Nerc>();
	}

	public List<Nerc> getNercList() {
		return podao.getNercList();
	}

	public List<PowOutages> faiAnalisi(Nerc nerc, int years, int hours) {
		List<PowOutages> poList = podao.getPowOutages(nerc.getId()); // prendo tutti i blackout avvenuti in quel nerc
		List<PowOutages> parziale = new ArrayList<>();
		this.res = new ArrayList<PowOutages>();
		cerca(parziale, poList, years, hours, 0);
		return res;
	}

	public void cerca(List<PowOutages> parziale, List<PowOutages> poList, int years, int hours, int L) {

		// casi terminali

		if (L == poList.size()) {
			return;
		}
		if (calcolaAnno(parziale) <= years && calcolaOre(parziale) <= hours) {

			this.res = new ArrayList<PowOutages>(parziale);
			System.out.println("Finito " + res.toString());
			return;
		}

		// genero sotto-problemi
		// blackout[L] è da aggiungere?
		// non lo aggiungo
		cerca(parziale, poList, years, hours, L + 1);

		// lo aggiungo
		parziale.add(poList.get(L));
		cerca(parziale, poList, years, hours, L + 1);
		parziale.remove(poList.get(L));
	}

	public int calcolaAnno(List<PowOutages> poList) {

		/*
		 * Date recentEnd = poList.get(0).getDateEnd(); //for(PowOutages po : poList) {
		 * //if(po.getDateEnd().after(recentEnd)) { //recentEnd = po.getDateEnd(); //}
		 * //} //Date oldStart = poList.get(0).getDateStart(); for(PowOutages po :
		 * poList) { if(po.getDateStart().before(oldStart)) { oldStart =
		 * po.getDateStart(); } } Calendar calEnd = Calendar.getInstance();
		 * calEnd.setTime(recentEnd); Calendar calStart = Calendar.getInstance();
		 * calStart.setTime(oldStart); return
		 * java.lang.Math.abs(calEnd.get(Calendar.YEAR)-calStart.get(Calendar.YEAR));
		 */
		if (poList.size() >= 2) {
			LocalDate recentEnd = poList.get(0).getDateEnd().toLocalDate();
			LocalDate oldStart = poList.get(0).getDateStart().toLocalDate();

			for (PowOutages po : poList) {
				LocalDate dt = po.getDateEnd().toLocalDate();
				if (dt.isAfter(recentEnd)) {
					recentEnd = dt;
				}
				if (dt.isBefore(oldStart)) {
					oldStart = dt;
				}
			}
			return recentEnd.getYear() - oldStart.getYear();
		} else
			return 10000;
	}

	public int calcolaOre(List<PowOutages> poList) {
		int sum = 0;
		for (PowOutages po : poList) {
			/*
			 * Calendar calEnd = Calendar.getInstance(); calEnd.setTime(po.getDateEnd());
			 * Calendar calStart = Calendar.getInstance();
			 * calStart.setTime(po.getDateStart());
			 * 
			 * sum = sum + java.lang.Math.abs((calEnd.get(Calendar.MILLISECOND) -
			 * calStart.get(Calendar.time))); System.out.println(sum);
			 */

			// sum = sum +
			// java.lang.Math.abs(po.getDateEnd().toLocalTime().until(po.getDateStart().toLocalTime(),
			// hours));
			LocalDateTime tempDateTime = LocalDateTime.from(po.getDateStart());
			System.out.println(tempDateTime);
			System.out.println(po.getDateEnd());
			sum += tempDateTime.until(po.getDateEnd(), ChronoUnit.HOURS);
			/*
			 * LocalTime l1 = po.getDateEnd().toLocalTime(); LocalTime l2 =
			 * po.getDateStart().toLocalTime(); sum = sum + Duration.between(l1,
			 * l2).toNanos();
			 */
			System.out.println(sum);
		}
		return sum;
	}
}
