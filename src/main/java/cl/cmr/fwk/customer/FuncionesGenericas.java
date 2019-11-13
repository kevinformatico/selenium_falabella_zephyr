package cl.cmr.fwk.customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FuncionesGenericas extends BaseTest{

	public static boolean isProcessRunning(String serviceName) {

		try {

			Process pro = Runtime.getRuntime().exec("tasklist");
			BufferedReader reader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				if (line.startsWith(serviceName + ".exe")) {
					return true;
				}
			}

		} catch (Exception e) {
			System.out.println("Error al validar procesos abiertos: " + e.getMessage());
		}

		return false;
	}
	
	public static void executeCMD(String cmd) {
		try {

			Runtime.getRuntime().exec(cmd);
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	public static void killProcess(String serviceName) {

		try {
			Runtime.getRuntime().exec("taskkill /F /IM " + serviceName + ".exe");
		} catch (IOException e) {
			System.out.println("Error al cerrar procesos: " + e.getMessage());
		}

	}

	public static void cerrarProceso(String proceso) {
		while(isProcessRunning(proceso)) {
			killProcess(proceso);
		}
	}
	
	public static String retornaFechaYYYYMMDD() {
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return sdf.format(date);
	}
	
	public static String retornaDia() {
		
		Calendar calendar = Calendar.getInstance();
		String dia = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		
		if(dia.length() == 1) {
			dia = "0" + dia;
		}
		
		return dia;
	}
	
	public static String retornaMes() {
		
		Calendar calendar = Calendar.getInstance();
		String mes = Integer.toString(calendar.get(Calendar.MONTH) + 1);
		
		if(mes.length() == 1) {
			mes = "0" + mes;
		}
		
		return mes;
	}
	
	public static String retornaNMes() {
		Calendar calendar = Calendar.getInstance();
		String month = new DateFormatSymbols().getShortMonths()[calendar.get(Calendar.MONTH)];
		return month;
	}
	
	public static int retornaAnio() {
		
		Calendar calendar = Calendar.getInstance();
		int anio = calendar.get(Calendar.YEAR);
		return anio;
	}
	
	public static String retornaFechaDDMMYYYY(int masDias) {
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(masDias);
	}
	
	public static String retornaHora() {
		DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
	}
	
	public static String retornaNombreDia() {

		Calendar now = Calendar.getInstance();
		 
//		System.out.println("Fecha actual : " + (now.get(Calendar.MONTH) + 1)
//						+ "-"
//						+ now.get(Calendar.DATE)
//						+ "-"
//						+ now.get(Calendar.YEAR));
 
		String[] strDays = new String[]{
						"Domingo",
						"Lunes",
						"Martes",
						"Miercoles",
						"Jueves",
						"Viernes",
						"Sabado"};
 
		return strDays[now.get(Calendar.DAY_OF_WEEK) - 1];
		
	}
	
	public static String retornaNombreMes(String mes) {
		
		String nomMes = null;
		
		switch (mes) {
			case "01":
				nomMes = "enero";
				break;
			case "02":
				nomMes = "febrero";
				break;
			case "03":
				nomMes = "marzo";
				break;
			case "04":
				nomMes = "abril";
				break;
			case "05":
				nomMes = "mayo";
				break;
			case "06":
				nomMes = "junio";
				break;
			case "07":
				nomMes = "julio";
				break;
			case "08":
				nomMes = "agosto";
				break;
			case "09":
				nomMes = "septiembre";
				break;
			case "10":
				nomMes = "octubre";
				break;
			case "11":
				nomMes = "noviembre";
				break;
			case "12":
				nomMes = "diciembre";
				break;
			}
		
		return nomMes;
		
	}
	
	public static String retornaNombreMesIngles(String mes) {
		
		String nomMes = null;
		
		switch (mes) {
			case "01":
				nomMes = "January";
				break;
			case "02":
				nomMes = "February";
				break;
			case "03":
				nomMes = "March";
				break;
			case "04":
				nomMes = "April";
				break;
			case "05":
				nomMes = "May";
				break;
			case "06":
				nomMes = "June";
				break;
			case "07":
				nomMes = "July";
				break;
			case "08":
				nomMes = "August";
				break;
			case "09":
				nomMes = "September";
				break;
			case "10":
				nomMes = "October";
				break;
			case "11":
				nomMes = "November";
				break;
			case "12":
				nomMes = "December";
				break;
			}
		
		return nomMes;
		
	}
}
