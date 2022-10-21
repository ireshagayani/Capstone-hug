namespace API.Helper
{
    public static class Utility
    {
        public static DateTime? ToCurrentDateTime(this DateTime? dateTime)
        {
            return dateTime.HasValue ? TimeZoneInfo.ConvertTimeFromUtc(dateTime.Value, System.TimeZoneInfo.Local) : dateTime ;
        }
    }
}
