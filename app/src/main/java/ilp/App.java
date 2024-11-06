package ilp;

import javassist.*;
import java.util.*;
import java.time.LocalDate;

public class App {

    public static void main(String[] args) {
        try {
            ClassPool pool = ClassPool.getDefault();

            // cria a classe AdvancedInvestment
            CtClass investmentClass = pool.makeClass("AdvancedInvestment");

            // adiciona os campos
            addFields(pool, investmentClass);

            // adiciona o construtor
            addConstructor(investmentClass);

            // adiciona os metodos getters e setters
            addGettersAndSetters(investmentClass);

            // adiciona os metodos financeiros
            addFinancialMethods(investmentClass);

            // adiciona os metodos de categorizacao
            addCategorizationMethods(investmentClass);

            // adiciona os metodos de avaliacao de risco
            addRiskAssessmentMethods(investmentClass);

            // adiciona os metodos de gestao de portfolio
            addPortfolioManagementMethods(investmentClass);

            // adiciona os metodos de analise de mercado
            addMarketAnalysisMethods(investmentClass);

            // adiciona os metodos de calculo de impostos
            addTaxCalculationMethods(investmentClass);

            // gera a classe
            Class<?> investmentGeneratedClass = investmentClass.toClass();
            Object investment1 = createInvestment(investmentGeneratedClass, "AAPL", 50, 150.0, 175.0, "Stock", LocalDate.now().minusMonths(6));
            Object investment2 = createInvestment(investmentGeneratedClass, "TSLA", 20, 600.0, 700.0, "Stock", LocalDate.now().minusYears(1));
            Object investment3 = createInvestment(investmentGeneratedClass, "BTC", 2, 30000.0, 35000.0, "Cryptocurrency", LocalDate.now().minusMonths(3));

            // analisa o portfolio
            List<Object> portfolio = new ArrayList<>(Arrays.asList(investment1, investment2, investment3));
            analyzePortfolio(portfolio, investmentGeneratedClass);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addFields(ClassPool pool, CtClass ctClass) throws CannotCompileException, NotFoundException {
        createField(pool, ctClass, "ticker", "java.lang.String");
        createField(pool, ctClass, "quantity", "int");
        createField(pool, ctClass, "purchasePrice", "double");
        createField(pool, ctClass, "currentPrice", "double");
        createField(pool, ctClass, "category", "java.lang.String");
        createField(pool, ctClass, "purchaseDate", "java.time.LocalDate");
        createField(pool, ctClass, "lastDividend", "double");
        createField(pool, ctClass, "volatility", "double");
    }

    private static CtField createField(ClassPool pool, CtClass ctClass, String name, String type) throws CannotCompileException, NotFoundException {
        CtField field = new CtField(pool.get(type), name, ctClass);
        field.setModifiers(Modifier.PRIVATE);
        ctClass.addField(field);
        return field;
    }

    private static void addConstructor(CtClass ctClass) throws CannotCompileException, NotFoundException {
        CtConstructor constructor = new CtConstructor(new CtClass[]{
                ClassPool.getDefault().get("java.lang.String"),
                CtClass.intType,
                CtClass.doubleType,
                CtClass.doubleType,
                ClassPool.getDefault().get("java.lang.String"),
                ClassPool.getDefault().get("java.time.LocalDate")
        }, ctClass);
        constructor.setBody("{" +
                "this.ticker = $1; " +
                "this.quantity = $2; " +
                "this.purchasePrice = $3; " +
                "this.currentPrice = $4; " +
                "this.category = $5; " +
                "this.purchaseDate = $6; " +
                "this.lastDividend = 0.0; " +
                "this.volatility = 0.0; " +
                "}");
        ctClass.addConstructor(constructor);
    }

    private static void addGettersAndSetters(CtClass ctClass) throws CannotCompileException {
        for (CtField field : ctClass.getDeclaredFields()) {
            String fieldName = field.getName();
            String capitalizedFieldName = capitalize(fieldName);
            ctClass.addMethod(CtNewMethod.getter("get" + capitalizedFieldName, field));
            ctClass.addMethod(CtNewMethod.setter("set" + capitalizedFieldName, field));
        }
    }

    private static void addFinancialMethods(CtClass ctClass) throws CannotCompileException {
        createMethod(ctClass, "calculateProfitLoss", CtClass.doubleType,
                "{ return (this.currentPrice - this.purchasePrice) * this.quantity; }");

        createMethod(ctClass, "calculateReturnPercentage", CtClass.doubleType,
                "{ return ((this.currentPrice - this.purchasePrice) / this.purchasePrice) * 100; }");

        createMethod(ctClass, "calculateAnnualizedReturn", CtClass.doubleType,
                "{ long days = java.time.temporal.ChronoUnit.DAYS.between(this.purchaseDate, java.time.LocalDate.now()); " +
                "double years = days / 365.0; " +
                "return Math.pow((1 + this.calculateReturnPercentage() / 100), 1 / years) - 1; }");

        createMethod(ctClass, "calculateDividendYield", CtClass.doubleType,
                "{ return (this.lastDividend / this.currentPrice) * 100; }");

    }

    private static void addCategorizationMethods(CtClass ctClass) throws CannotCompileException, NotFoundException {
        createMethod(ctClass, "categorizePerformance", ClassPool.getDefault().get("java.lang.String"),
                "{ double annualizedReturn = this.calculateAnnualizedReturn(); " +
                "if (annualizedReturn > 20) return \"Excelente\"; " +
                "else if (annualizedReturn > 10) return \"Bom\"; " +
                "else if (annualizedReturn > 0) return \"Regular\"; " +
                "else return \"Ruim\"; }");

        createMethod(ctClass, "categorizeSector", ClassPool.getDefault().get("java.lang.String"),
                "{ if (this.category.equals(\"Stock\")) { " +
                "    if (this.ticker.startsWith(\"A\")) return \"Tecnologia\"; " +
                "    else if (this.ticker.startsWith(\"B\")) return \"Finanças\"; " +
                "    else return \"Outros\"; " +
                "} else return this.category; }");
    }

    private static void addRiskAssessmentMethods(CtClass ctClass) throws CannotCompileException, NotFoundException {
        createMethod(ctClass, "calculateRiskScore", CtClass.doubleType,
                "{ return this.volatility * 10; }");

        createMethod(ctClass, "assessRiskLevel", ClassPool.getDefault().get("java.lang.String"),
                "{ double riskScore = this.calculateRiskScore(); " +
                "if (riskScore > 8) return \"Alto Risco\"; " +
                "else if (riskScore > 5) return \"Médio Risco\"; " +
                "else return \"Baixo Risco\"; }");
    }

    private static void addPortfolioManagementMethods(CtClass ctClass) throws CannotCompileException {
       
        createMethod(ctClass, "getCurrentValue", CtClass.doubleType,
                "{ return this.currentPrice * this.quantity; }");

        createMethod(ctClass, "calculateWeightInPortfolio", CtClass.doubleType,
                "{ return this.getCurrentValue() / $1; }", new CtClass[]{CtClass.doubleType});

        createMethod(ctClass, "shouldRebalance", CtClass.booleanType,
                "{ return Math.abs(this.calculateWeightInPortfolio($1) - $2) > 0.05; }",
                new CtClass[]{CtClass.doubleType, CtClass.doubleType});
    }

    private static void addMarketAnalysisMethods(CtClass ctClass) throws CannotCompileException {
        createMethod(ctClass, "calculatePriceToEarningsRatio", CtClass.doubleType,
                "{ return this.currentPrice / ($1 / this.quantity); }", new CtClass[]{CtClass.doubleType});

        createMethod(ctClass, "isOvervalued", CtClass.booleanType,
                "{ return this.calculatePriceToEarningsRatio($1) > 25; }", new CtClass[]{CtClass.doubleType});
    }

    private static void addTaxCalculationMethods(CtClass ctClass) throws CannotCompileException {
        createMethod(ctClass, "calculateCapitalGainsTax", CtClass.doubleType,
                "{ double profit = this.calculateProfitLoss(); " +
                "return profit > 0 ? profit * 0.15 : 0; }");

        createMethod(ctClass, "calculateDividendTax", CtClass.doubleType,
                "{ return this.lastDividend * this.quantity * 0.10; }");
    }

    private static CtMethod createMethod(CtClass ctClass, String name, CtClass returnType, String body) throws CannotCompileException {
        return createMethod(ctClass, name, returnType, body, new CtClass[]{});
    }

    private static CtMethod createMethod(CtClass ctClass, String name, CtClass returnType, String body, CtClass[] parameters) throws CannotCompileException {
        CtMethod method = new CtMethod(returnType, name, parameters, ctClass);
        method.setModifiers(Modifier.PUBLIC);
        method.setBody(body);
        ctClass.addMethod(method);
        return method;
    }

    private static Object createInvestment(Class<?> investmentClass, String ticker, int quantity, double purchasePrice, double currentPrice, String category, LocalDate purchaseDate) throws Exception {
        return investmentClass.getConstructor(String.class, int.class, double.class, double.class, String.class, LocalDate.class)
                .newInstance(ticker, quantity, purchasePrice, currentPrice, category, purchaseDate);
    }

    private static void analyzePortfolio(List<Object> portfolio, Class<?> investmentClass) {
        try {
            double totalPortfolioValue = calculateTotalPortfolioValue(portfolio, investmentClass);
            System.out.println("Análise de Portfólio");
            System.out.println("--------------------");
            System.out.println("Valor Total do Portfólio: $" + String.format("%.2f", totalPortfolioValue));

            for (Object investment : portfolio) {
                analyzeInvestment(investment, investmentClass, totalPortfolioValue);
            }

            System.out.println("\nRecomendações de Rebalanceamento:");
            for (Object investment : portfolio) {
                String ticker = (String) investmentClass.getMethod("getTicker").invoke(investment);
                double targetWeight = 1.0 / portfolio.size();
                boolean shouldRebalance = (boolean) investmentClass.getMethod("shouldRebalance", double.class, double.class)
                        .invoke(investment, totalPortfolioValue, targetWeight);
                if (shouldRebalance) {
                    System.out.println("- Rebalancear " + ticker + " para atingir o peso alvo de " + String.format("%.2f%%", targetWeight * 100));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double calculateTotalPortfolioValue(List<Object> portfolio, Class<?> investmentClass) throws Exception {
        double total = 0;
        for (Object investment : portfolio) {
            total += (double) investmentClass.getMethod("getCurrentValue").invoke(investment);
        }
        return total;
    }

    private static void analyzeInvestment(Object investment, Class<?> investmentClass, double totalPortfolioValue) {
        try {
            String ticker = (String) investmentClass.getMethod("getTicker").invoke(investment);
            double profitLoss = (double) investmentClass.getMethod("calculateProfitLoss").invoke(investment);
            double returnPercentage = (double) investmentClass.getMethod("calculateReturnPercentage").invoke(investment);
            String performance = (String) investmentClass.getMethod("categorizePerformance").invoke(investment);
            String sector = (String) investmentClass.getMethod("categorizeSector").invoke(investment);
            String riskLevel = (String) investmentClass.getMethod("assessRiskLevel").invoke(investment);
            double weight = (double) investmentClass.getMethod("calculateWeightInPortfolio", double.class).invoke(investment, totalPortfolioValue);
            double capitalGainsTax = (double) investmentClass.getMethod("calculateCapitalGainsTax").invoke(investment);

            System.out.println("\nInvestimento: " + ticker);
            System.out.println("Lucro/Prejuízo: $" + String.format("%.2f", profitLoss));
            System.out.println("Retorno Percentual: " + String.format("%.2f%%", returnPercentage));
            System.out.println("Desempenho: " + performance);
            System.out.println("Setor: " + sector);
            System.out.println("Nível de Risco: " + riskLevel);
            System.out.println("Peso no Portfólio: " + String.format("%.2f%%", weight * 100));
            System.out.println("Imposto sobre Ganhos de Capital: $" + String.format("%.2f", capitalGainsTax));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}