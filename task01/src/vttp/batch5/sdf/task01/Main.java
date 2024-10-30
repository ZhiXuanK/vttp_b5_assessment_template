package vttp.batch5.sdf.task01;

import java.io.*;
import java.util.*;
import vttp.batch5.sdf.task01.models.BikeEntry;
// Use this class as the entry point of your program

public class Main {

	public static void main(String[] args) {
		//System.out.printf("hello, world\n");

		//args[0] is the name of csv file
		//take in csv file
		File file = new File(args[0]);

		//List of all bike entry
		List<BikeEntry> data = new ArrayList<>();

		//take in file and put into data
		try (Reader reader = new FileReader(file);
		BufferedReader br = new BufferedReader(reader);) {
			//read the headers, don't need in bike entry
			String line = br.readLine();

			while ((line = br.readLine()) != null){
				String[] currLine = line.split(",");
				/*season,mnth,holiday,weekday,weathersit,temp,hum,windspeed,casual,registered */
				BikeEntry current = BikeEntry.toBikeEntry(currLine);
				data.add(current);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		//count
		int[][] counted = new int[data.size()][6];
		int c = 0;
		for (BikeEntry b:data){
			int[] current = new int[6];
			current[0] = b.getSeason();
			current[1] = b.getWeekday();
			current[2] = b.getMonth();
			current[3] = b.getWeather();
			current[4] = boolToInt(b.isHoliday());
			current[5] = b.getCasual() + b.getRegistered();

			counted[c] = current;
			c += 1;
		}

		Arrays.sort(counted, Comparator.comparingInt(a -> a[5]));

		int pos = counted.length;
		int positionPlace = 0;
		String[] positionList = {"highest", "second highest", "third highest", "fourth highest", "fifth highest"};

		while (pos > (counted.length - 5)){
			pos = pos - 1;
			
			int[] current = counted[pos];
			String position = positionList[positionPlace];
			String season = Utilities.toSeason(current[0]);
			String day = Utilities.toWeekday(current[1]);
			String month = Utilities.toMonth(current[2]);
			int total = current[5];
			String weather = "";
			switch (current[3]){
				case 1:
					weather = "Clear, Few clouds, Partly cloudy, Partly cloudy";
					break;
				case 2:
					weather = "Mist + Cloudy, Mist + Broken clouds, Mist + Few clouds, Mist";
					break;
				case 3:
					weather = "Light Snow, Light Rain + Thunderstorm + Scattered clouds, Light Rain + Scattered clouds";
					break;
				case 4:
					weather = "Heavy Rain + Ice Pallets + Thunderstorm + Mist, Snow + Fog";
					break;
			}
			String holiday ="";
			if (current[4] == 0){
				holiday = "not a holiday";
			} else if (current[4] == 1){
				holiday = "a holiday";
			}

			System.out.printf(
			"The %s (position) recorded number of cyclists was in %s (season), on a %s (day) in the month of %s (month).\nThere were a total of %d (total) cyclist. The weather was %s (weather). \n%s (day) was %s.\n\n"	
			, position, season, day, month, total, weather, day, holiday);

			positionPlace += 1;
		}
	}

	public static int boolToInt(boolean b){
		if (b){
			return 1;
		} else {
			return 0;
		}
	}
	
}