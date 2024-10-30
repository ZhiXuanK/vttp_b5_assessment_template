package vttp.batch5.sdf.task01;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
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
		
		//group the data based on seaon, day, month, weather and find the count
		String[] conditions = new String[5]; //season, day, month, weather, holiday

		//use first stream to group by season, day, month, weather, holiday
		Map<List<Integer>, List<BikeEntry>> grouped = data.stream()
													.collect(Collectors.groupingBy(b ->List.of(b.getSeason(), b.getWeekday(), b.getMonth(), b.getWeather(), boolToInt(b.isHoliday()))));
		

		//Comparator<Product> comparator = Comparator.comparing(Product::getRating)
		//Map<List<Integer>, Long> sort = grouped.entrySet().stream().sorted((g1,g2) -> Long.compare(g1.getValue(), g2.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (g1, g2) -> g1, LinkedHashMap::new));
		//Map<List<Integer>, Long> reverse = new HashMap<>();

		//get total variations
		List<Integer[]> dataProcessed = new ArrayList<>();
		
		for (List<Integer> m:grouped.keySet()){
			int totalCount = 0;
			List<BikeEntry> rows = grouped.get(m);
			//Integer[] fromAbove = new Integer[6];
			//Integer[] fromAbove2 = new Integer[6];
			//add total count of all entries
			for (int i=0; i<rows.size(); i++){
				totalCount = totalCount + rows.get(i).getCasual() + rows.get(i).getRegistered();
			}
			Integer[] current = new Integer[m.size()+1];
			for (int i=0; i < 5; i++){
				current[i] = m.get(i);
			}
			current[5] = totalCount;
			dataProcessed.add(current);
		}
		
		//using data processed, sort the list based on value of dataProcessed[][6]
		//Comparator<Integer[]> comparator = Comparator.comparing(Integer[5]);
		int[][] forSorting = new int[dataProcessed.size()][6];
		for (int i =0; i < dataProcessed.size(); i++){
			for (int j = 0; j < 6; j ++){
				forSorting[i][j] = dataProcessed.get(i)[j];
			}
		}

		Arrays.sort(forSorting, Comparator.comparingInt(a -> a[5]));

		int pos = forSorting.length;
		int positionPlace = 0;
		String[] positionList = {"highest", "second highest", "third highest", "fourth highest", "fifth highest"};

		while (pos > (forSorting.length - 5)){
			pos = pos - 1;
			
			int[] current = forSorting[pos];
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
			String holiday;
			if (current[4] == 0){
				holiday = "not a holiday";
			} else if (current[4] == 1){
				holiday = "a holiday";
			}

			System.out.printf(
			"The %s (position) recorded number of cyclists was in %s (season), on a %s (day) in the month of %s (month).\nThere were a total of %d (total) cyclists. The weather was %s (weather). \n%s (day) was not a holiday.\n\n"	
			, position, season, day, month, total, weather, day);

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