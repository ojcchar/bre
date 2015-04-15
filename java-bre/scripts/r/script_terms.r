root = 'C:/Users/oxc140530/Documents/Google Drive/UTDALLAS/CS 6301 - Soft. Comprehension and Analysis/Project/BRE/Results/terms/'
systems = c('jabref', 'ofbiz', 'openbravo')

system = systems[1]
terms_file = paste(root, 'terms_', system, '.csv', sep = "")


#read the csv
data_terms = read.csv(terms_file, sep = ";", header = TRUE)

freq = table(data_terms$Lemma)
freq_frame = as.data.frame(freq)
barplot(freq[freq > quantile(freq_frame$Freq, .75) ])


barplot(freq[freq > 5000 ])

#--------------------------------

freq2 = table(data_terms$Lemma, data_terms$File)
freq2 = as.data.frame(freq2)
colnames(freq2) <- c("Lemma", "File", "Frequencies")
freq2 = subset(freq2, Frequencies!=0)
freq2  = table(freq2$Lemma)
freq2 = as.data.frame(freq2)
colnames(freq2) <- c("Lemma", "Frequencies")
freq2 = subset(freq2, Frequencies!=0)
freq2 = freq2[!duplicated(freq2[,]), ]

sort_freq2 = freq2[with(freq2, order(-Frequencies)), ]
sort_freq21 = freq2[with(freq2, order( Frequencies)), ]

#--------------------------------
#dummy
View(freq_frame[with(freq_frame, order(-Freq)), ])


summary(freq_frame$Freq)

boxplot(freq_frame$Freq)


View(freq2)