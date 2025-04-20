LOG_FILE="$1"

# Check if the file exists
if [ ! -f "$LOG_FILE" ]; then
    echo "Error: File '$LOG_FILE' not found!"
    exit 1
fi

tr -cd '[:print:]\n' < "$LOG_FILE" | sed 's/[^[:print:]\t]//g' > cleaned_log.txt

mv cleaned_log.txt "$LOG_FILE"

grep -v ' 200 ' "$LOG_FILE" | awk '{print $1}' | sort | uniq