root = 'C:/Users/oxc140530/Documents/Google Drive/UTDALLAS/CS 6301 - Soft. Comprehension and Analysis/Project/BRE/Results/terms/'
systems = c('jabref', 'ofbiz', 'openbravo')

top_results = 5
top_perc = 0.8

for(k in 1:length(systems)){
  
  system = systems[k]
  patterns_file = paste(root, 'patterns_', system, '.csv', sep = "")
  
  #read the csv
  data_patt = read.csv(patterns_file, sep = ";", header = TRUE)
  
  #group by Statement.Type, POS.Pattern
  patt_freq = as.data.frame(table(data_patt$Statement.Type, data_patt$POS.Pattern))
  colnames(patt_freq) <- c("Statement.Type", "POS.Pattern", "Frequencies")
  patt_freq = subset(patt_freq, Frequencies!=0)
  patt_freq$row.names = NULL
  
  #sorted by statement and frequency(desc)
  patt_freq = patt_freq[with(patt_freq, order(Statement.Type, -Frequencies)), ]
  
  #-------------------------------------------
  stmts = c('MethodDeclaration','TypeDeclaration', 'SingleVariableDeclaration', 'VariableDeclarationFragment', 'EnumDeclaration', 'EnumConstantDeclaration')
  
  for (j in 1:length(stmts) ) {
    stmt = stmts[j]
    freq_sub = subset(patt_freq, Statement.Type == stmt)
    num = nrow(freq_sub)
    freq_tot = sum(freq_sub$Frequencies)
    
    accum_perc = 0;
    
    pos_types = c()
    
    for (i in 1:num ) {
      
      perc =  freq_sub[i,]$Frequencies/freq_tot
      accum_perc = accum_perc + perc
      if (accum_perc > top_perc || i > top_results){
        break;
      }
      pos_types <- c(pos_types,  as.character(freq_sub$POS.Pattern[i]))
    }
    
    data_sub = subset(data_patt, Statement.Type == stmt &  POS.Pattern %in% pos_types)
    data_sub = data_sub[!duplicated(data_sub[,]), ]
    data_sub = data_sub[with(data_sub, order(Statement.Type, POS.Pattern, NL.Pattern)), ]
    data_sub$row.names = NULL
    
    write.csv(data_sub, file=paste(root, 'patterns_', stmt,"_", system, '_' , format(accum_perc,digits=2), '.csv', sep = ""), row.names = FALSE)
  
  }

}

#-------------------------------------------

