data = read.csv("C:/Users/oxc140530/Documents/Workspaces/Git/bre/java-bre/data_jabref.csv", sep = ";", header = TRUE)

tags_st = data[,c(1,2,5)]
write.csv(tags_st, file="C:/Users/oxc140530/Desktop/text.csv")


freq_tags_st = table( tags_st$Statement.Type, tags_st$POS.Tag)

View(freq_tags_st)

summary(data$POS.Tag)

freq = table(data$POS.Tag)
barplot(freq)


freq = table(data$Lemma, data$POS.Tag)
barplot(freq)

freq = table(data$Lemma)
barplot(freq[freq>15 ])


barplot(freq[Var2=='CC' ])

freq1 = data.frame(freq)
barplot(freq1$Var2=='CC')

View(freq)

freq[freq>15]
mean(freq)