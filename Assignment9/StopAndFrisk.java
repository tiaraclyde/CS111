import java.util.ArrayList;

/**
 * The StopAndFrisk class represents stop-and-frisk data, provided by
 * the New York Police Department (NYPD), that is used to compare
 * during when the policy was put in place and after the policy ended.
 * 
 * @author Tanvi Yamarthy
 * @author Vidushi Jindal
 */
public class StopAndFrisk {

    /*
     * The ArrayList keeps track of years that are loaded from CSV data file.
     * Each SFYear corresponds to 1 year of SFRecords. 
     * Each SFRecord corresponds to one stop and frisk occurrence.
     */ 
    private ArrayList<SFYear> database; 

    /*
     * Constructor creates and initializes the @database array
     * 
     * DO NOT update nor remove this constructor
     */
    public StopAndFrisk () {
        database = new ArrayList<>();
    }

    /*
     * Getter method for the database.
     * *** DO NOT REMOVE nor update this method ****
     */
    public ArrayList<SFYear> getDatabase() {
        return database;
    }

    /**
     * This method reads the records information from an input csv file and populates 
     * the database.
     * 
     * Each stop and frisk record is a line in the input csv file.
     * 
     * 1. Open file utilizing StdIn.setFile(csvFile)
     * 2. While the input still contains lines:
     *    - Read a record line (see assignment description on how to do this)
     *    - Create an object of type SFRecord containing the record information
     *    - If the record's year has already is present in the database:
     *        - Add the SFRecord to the year's records
     *    - If the record's year is not present in the database:
     *        - Create a new SFYear 
     *        - Add the SFRecord to the new SFYear
     *        - Add the new SFYear to the database ArrayList
     * 
     * @param csvFile
     */
    public void readFile ( String csvFile ) {

        // DO NOT remove these two lines
        StdIn.setFile(csvFile); // Opens the file
        StdIn.readLine();       // Reads and discards the header line
        while(!StdIn.isEmpty()){

            String[] recordEntries = StdIn.readLine().split(",");
            int year = Integer.parseInt(recordEntries[0]);
            String description = recordEntries[2];
            Boolean arrested = recordEntries[13].equals("Y");
            Boolean frisked = recordEntries[16].equals("Y");
            String gender = recordEntries[52];
            String race = recordEntries[66];
            String location = recordEntries[71];
            SFRecord sfrecord = new SFRecord(description, arrested, frisked, gender, race, location);
           int databaseSize = database.size();
           boolean yearExists = false;

           for(int i =0; i<databaseSize; i++){
            SFYear SfYear = database.get(i);
            if(SfYear.getcurrentYear() == year){
                SfYear.addRecord(sfrecord);
                yearExists = true;
            }
           }
           if(!yearExists){
            SFYear newSFyear = new SFYear(year);
            newSFyear.addRecord(sfrecord);
            database.add(newSFyear);
           }
        }
        

    }

    /**
     * This method returns the stop and frisk records of a given year where 
     * the people that was stopped was of the specified race.
     * 
     * @param year we are only interested in the records of year.
     * @param race we are only interested in the records of stops of people of race. 
     * @return an ArrayList containing all stop and frisk records for people of the 
     * parameters race and year.
     */

    public ArrayList<SFRecord> populationStopped ( int year, String race ) {
        ArrayList<SFRecord> result = new ArrayList<>();
        for(int i = 0; i < database.size(); i++){
            SFYear sfYear = database.get(i);

            if(sfYear.getcurrentYear() == year){
                ArrayList<SFRecord> recordsForYear = sfYear.getRecordsForYear();

                for(int j = 0; j< recordsForYear.size(); j++){
                    SFRecord sfRecord = recordsForYear.get(j);
                    if(sfRecord.getRace().equals(race)){
                        result.add(sfRecord);
                    }
                }
            }
            
            
        }
        return result;

    }

    /**
     * This method computes the percentage of records where the person was frisked and the
     * percentage of records where the person was arrested.
     * 
     * @param year we are only interested in the records of year.
     * @return the percent of the population that were frisked and the percent that
     *         were arrested.
     */
    public double[] friskedVSArrested ( int year ) {
        
        double friskedPercentage = 0.0;
        double arrestedPercentage = 0.0;

        for(int i = 0; i < database.size(); i++){
            SFYear sfYear = database.get(i);

            if(sfYear.getcurrentYear() == year){
                ArrayList<SFRecord> recordsForYear = sfYear.getRecordsForYear();

                int friskedNum = 0;
                int arrestedNum = 0;

                for (int j = 0; j < recordsForYear.size(); j++){
                    SFRecord sfRecord = recordsForYear.get(j);

                    if(sfRecord.getFrisked()){
                        friskedNum++;
                    }
                    if(sfRecord.getArrested()){
                        arrestedNum++;
                    }
                }
                friskedPercentage = (friskedNum * 100.0) / recordsForYear.size();
                arrestedPercentage = (arrestedNum * 100.0) / recordsForYear.size();
            }
        }


        double[] total = {friskedPercentage, arrestedPercentage};
        return total; // update the return value
    }

    /**
     * This method keeps track of the fraction of Black females, Black males,
     * White females and White males that were stopped for any reason.
     * Drawing out the exact table helps visualize the gender bias.
     * 
     * @param year we are only interested in the records of year.
     * @return a 2D array of percent of number of White and Black females
     *         versus the number of White and Black males.
     */
    public double[][] genderBias ( int year ) {

        int blackNum = 0;
        int whiteNum = 0;
        int blackMaleNum = 0;
        int whiteMaleNum = 0;
        int blackFemaleNum = 0;
        int whiteFemaleNum = 0;

        for(int i = 0; i < database.size(); i++){
            SFYear sfYear = database.get(i);

            if (sfYear.getcurrentYear()==year){
                ArrayList<SFRecord> recordsForYear = sfYear.getRecordsForYear();

                 for (int j = 0; j < recordsForYear.size(); j++){
                    SFRecord sfRecord = recordsForYear.get(j);

                    if (sfRecord.getRace().equals("B")){
                        blackNum++;
                
                    if (sfRecord.getGender().equals("M")){
                        blackMaleNum++;
                    }
                    else if (sfRecord.getGender().equals("F")){
                        blackFemaleNum++;
                    }
                } else if (sfRecord.getRace().equals("W")){
                        whiteNum++;
                    
                    if (sfRecord.getGender().equals("M")){
                        whiteMaleNum++;
                    }
                    else if (sfRecord.getGender().equals("F")){
                        whiteFemaleNum++;
                    }
            }
        }
    }
}



        double [][] result = {
            {(blackFemaleNum / (double) blackNum) * 0.5 * 100, (whiteFemaleNum / (double) whiteNum) * 0.5 * 100,
            ((blackFemaleNum / (double) blackNum * 0.5 * 100) + (whiteFemaleNum / (double) whiteNum) * 0.5 * 100)},
            {(blackMaleNum / (double) blackNum) * 0.5 * 100, (whiteMaleNum / (double) whiteNum) * 0.5 * 100,
            ((blackMaleNum / (double) blackNum * 0.5 * 100) + (whiteMaleNum / (double) whiteNum) * 0.5 * 100)}

        };
        return result; // update the return value
    }

    
    /**
     * This method checks to see if there has been increase or decrease 
     * in a certain crime from year 1 to year 2.
     * 
     * Expect year1 to preceed year2 or be equal.
     * 
     * @param crimeDescription
     * @param year1 first year to compare.
     * @param year2 second year to compare.
     * @return 
     */

    public double crimeIncrease ( String crimeDescription, int year1, int year2 ) {
        if(year1 >= year2){
            System.out.println("year 1 needs to be less than year 2");
            return 0;
        }
        int CountYear1 = 0;
        int CountYear2 = 0;
        int total1 = 0;
        int total2 = 0;

        for(int i = 0; i < database.size(); i++){
            SFYear sfYear = database.get(i);

            if(sfYear.getcurrentYear()==year1){
                 ArrayList<SFRecord> recordsForYear = sfYear.getRecordsForYear();
                  for (int j = 0; j < recordsForYear.size(); j++){
                    SFRecord sfRecord = recordsForYear.get(j);
                    if(sfRecord.getDescription().indexOf(crimeDescription) != -1){
                        CountYear1++;
                    }
                    total1++;
            }
        }
          if(sfYear.getcurrentYear()==year2){
                 ArrayList<SFRecord> recordsForYear = sfYear.getRecordsForYear();
                  for (int j = 0; j < recordsForYear.size(); j++){
                    SFRecord sfRecord = recordsForYear.get(j);
                    if(sfRecord.getDescription().indexOf(crimeDescription) != -1){
                        CountYear2++;
                    }
                    total2++;
                }
            }
        }
        double percentageChange = ((CountYear2/ (double) total2) - (CountYear1 / (double) total1)) * 100.0;
	return percentageChange; // update the return value
    }

    /**
     * This method outputs the NYC borough where the most amount of stops 
     * occurred in a given year. This method will mainly analyze the five 
     * following boroughs in New York City: Brooklyn, Manhattan, Bronx, 
     * Queens, and Staten Island.
     * 
     * @param year we are only interested in the records of year.
     * @return the borough with the greatest number of stops
     */
    public String mostCommonBorough ( int year ) {

        int[] counts = new int[5];
        String[] boroughs = {"Brooklyn", "Manhattan", "Bronx", "Queens", "Staten Island"};

        for(int i = 0; i < database.size(); i++){
            SFYear sfYear = database.get(i);

            if (sfYear.getcurrentYear()==year){
                ArrayList<SFRecord> recordsForYear = sfYear.getRecordsForYear();

                 for (int j = 0; j < recordsForYear.size(); j++){
                    SFRecord sfRecord = recordsForYear.get(j);
                    String location = sfRecord.getLocation().toUpperCase();

                    for(int k = 0; k < boroughs.length; k++){
                        if(location.equalsIgnoreCase(boroughs[k])){
                            counts[k]++;
                            break;
                        }
                    }
                }
    }
}
            int max = 0;
        for (int i= 1; i < counts.length; i++){
          if(counts[i] > counts[max]){
         max = i;
    }
}        

        return boroughs[max]; // update the return value
}
}