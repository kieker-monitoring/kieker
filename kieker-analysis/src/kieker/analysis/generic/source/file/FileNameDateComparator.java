package kieker.analysis.generic.source.file;

import java.io.File;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class FileNameDateComparator implements Comparator<File> {

    private static final String FILENAME_PATTERN = "(\\d{8}-\\d{9})-UTC-\\d{3}";

    @Override
    public int compare(File file1, File file2) {
        String fileName1 = file1.getName();
        String fileName2 = file2.getName();

        String dateStr1 = extractDateFromFileName(fileName1);
        String dateStr2 = extractDateFromFileName(fileName2);

        // Parse the date strings and compare them
        long date1 = parseDate(dateStr1);
        long date2 = parseDate(dateStr2);

        if (date1 < date2) {
            return -1;
        } else if (date1 > date2) {
            return 1;
        } else {
            return 0;
        }
    }

    private String extractDateFromFileName(String fileName) {
    	String filenameWithoutSuffix = fileName.substring("kieker-".length(), fileName.lastIndexOf('.'));
    	
        Pattern pattern = Pattern.compile(FILENAME_PATTERN);
        Matcher matcher = pattern.matcher(filenameWithoutSuffix);

        if (matcher.matches()) {
            return matcher.group(1);
        }

        return null;
    }

    private long parseDate(String dateStr) {
        int year = Integer.parseInt(dateStr.substring(0, 4));
        int month = Integer.parseInt(dateStr.substring(4, 6));
        int day = Integer.parseInt(dateStr.substring(6, 8));

        return year * 10000 + month * 100 + day;
    }

}