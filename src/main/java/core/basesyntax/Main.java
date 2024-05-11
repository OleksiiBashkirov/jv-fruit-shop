package core.basesyntax;

import core.basesyntax.dao.FruitsDao;
import core.basesyntax.dao.FruitsDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.services.ReportGenerator;
import core.basesyntax.services.ReportService;
import core.basesyntax.services.TransactionProcessor;
import core.basesyntax.services.TransactionService;
import core.basesyntax.transactions.FruitsTransaction;
import core.basesyntax.util.CsvParser;
import core.basesyntax.util.CsvReader;
import core.basesyntax.util.CsvWriter;
import java.util.List;

public class Main {
    private static final String INPUT_FILE_PATH = "src/main/resources/inputFile.csv";
    private static final String OUTPUT_FILE_PATH = "src/main/resources/outputFile.csv";

    public static void main(String[] args) {
        Storage storage = new Storage();
        FruitsDao fruitsDao = new FruitsDaoImpl(storage);
        TransactionService transactionService = new TransactionProcessor(fruitsDao);
        ReportService reportService = new ReportGenerator(fruitsDao);

        List<String[]> rawLines = CsvReader.readLines((INPUT_FILE_PATH));
        List<FruitsTransaction> transactions = CsvParser.parseTransactions(rawLines);
        transactionService.processTransactions(transactions);
        String report = reportService.createReport();
        CsvWriter.writeReport(OUTPUT_FILE_PATH, report);
    }
}