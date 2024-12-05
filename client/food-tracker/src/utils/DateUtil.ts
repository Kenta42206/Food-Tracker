// YYYY-MM-DD形式の本日の日付文字列を戻す
export const getToday = () => {
  // sv-SEロケールはスウェーデンの標準日付形式でYYYY-MM-DDを戻す
  return new Date().toLocaleDateString("sv-SE");
};

// YYYY-MM-DD形式の本日の日付文字列を戻す
export const formatDate = (date: Date) => {
  // sv-SEロケールはスウェーデンの標準日付形式でYYYY-MM-DDを戻す
  return date.toLocaleDateString("sv-SE");
};

// YYYY-MM-DDTHH:mm:ss.sssZ を YYYY-MM-DDに変換したStringを戻す。
export const formatDateTimeToDateString = (date: Date) => {
  return date.toISOString().split("T")[0];
};

// YYYY-MM-DDTHH:mm:ss.ss を YYYY/MM/DD hh:mm に変換して戻す
export const formatDateTimeToDateTimeString = (dateString: string) => {
  const date = dateString.split("T")[0]; // "2000-01-01"
  const time = dateString.split("T")[1].split(":").slice(0, 2).join(":"); // "20:15" (秒数を取り除く)

  return `${date} ${time}`;
};

// YYYY-MM-DDTHH:mm:ss.ss を HH:mm に変換して戻す
export const formatTimeToHoursMinutes = (dateString: string) => {
  const time = dateString.split("T")[1].split(":").slice(0, 2).join(":"); // "20:15" (秒数を取り除く)
  return time;
};
