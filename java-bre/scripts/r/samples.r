root = 'C:/Users/oxc140530/Documents/Google Drive/UTDALLAS/CS 6301 - Soft. Comprehension and Analysis/Project/BRE/Results/rules/final_rules/'
#file_name = 'rules_openbravo_nofilters'
#n_sample = 100
#file_name = 'rules_openbravo'
file_name = 'rules_openbravo1'
n_sample = 200
patterns_file = paste(root, file_name,'.csv', sep = "")

#read the csv
data = read.csv(patterns_file, sep = ";", header = TRUE)

#summary
data = unique(data[,c(1,2)])
summary(data$Rule.Type)
nrow(data)

d_cat = subset(data, data$Rule.Type =="CATEG_ENUMERATION")
d_sym = subset(data, data$Rule.Type =="SYMBOL_LITERAL")
d_val = subset(data, data$Rule.Type =="VALID_VALUE")

smpl_c <- d_cat[sample(nrow(d_cat), if (n_sample > nrow(d_cat) ) nrow(d_cat) else n_sample),]
smpl_s <- d_sym[sample(nrow(d_sym), if (n_sample > nrow(d_sym) ) nrow(d_sym) else n_sample),]
smpl_v <- d_val[sample(nrow(d_val), if (n_sample > nrow(d_val) ) nrow(d_val) else n_sample),]

sample = rbind(smpl_s, smpl_c, smpl_v)
summary(sample$Rule.Type)

write.csv2(sample, file=paste(root, file_name, "_sample.csv", sep = ""), row.names = FALSE)
