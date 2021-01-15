public class PathShortener {

    private static final String SHORTENER_BACKSLASH_REGEX = "\\\\";
    private static final String SHORTENER_SLASH_REGEX = "/";
    private static final String SHORTENER_BACKSLASH = "\\";
    private static final String SHORTENER_SLASH = "/";
    private static final String SHORTENER_ELLIPSE = "...";

    public PathShortener() {}

    /**
     * Return shorter path based on the limited threshold
     * @param path Path to specific file
     * @param threshold Limit of directories included in the path
     * @return Shortened path to specific directory
     */
    public String pathShortener(String path, int threshold) {

        String regex = SHORTENER_BACKSLASH_REGEX;
        String sep = SHORTENER_BACKSLASH;

        if (path.indexOf("/") > 0) {
            regex = SHORTENER_SLASH_REGEX;
            sep = SHORTENER_SLASH;
        }

        String pathtemp[] = path.split(regex);
        // remove empty elements
        int elem = 0;
        {
            String newtemp [] = new String [pathtemp.length];
            int j = 0;
            for (int i=0; i < pathtemp.length; i++) {
                if (!pathtemp[i].equals("")) {
                    newtemp [j++] = pathtemp[i];
                    elem++;
                }
            }
            pathtemp = newtemp;
        }


        if (elem > threshold) {
            StringBuilder sb = new StringBuilder();
            int index = 0;

            // drive or protocol
            int pos2dots = path.indexOf(":");
            if (pos2dots > 0) {
                // case c:\ c:/ etc.
                sb.append(path.substring(0, pos2dots + 2));
                index++;
                // case http:// ftp:// etc.
                if (path.indexOf(":/") > 0 && pathtemp[0].length() > 2) {
                    sb.append(SHORTENER_SLASH);
                }
            }
            else {
                boolean isUNC = path.substring(0,2).equals(SHORTENER_BACKSLASH_REGEX);
                if (isUNC) {
                    sb.append(SHORTENER_BACKSLASH).append(SHORTENER_BACKSLASH);
                }
            }

            for (; index <= threshold; index++) {
                sb.append(pathtemp[index]).append(sep);
            }

            if (index == (elem - 1)) {
                sb.append(pathtemp[elem - 1]);
            }
            else {
                sb.append(SHORTENER_ELLIPSE)
                        .append(sep)
                        .append(pathtemp[elem - 1]);
            }
            return sb.toString();
        }
        return path;
    }
}
