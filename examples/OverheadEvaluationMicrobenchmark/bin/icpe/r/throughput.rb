def median(ary)
  middle = ary.size/2
  sorted = ary.sort_by{ |a| a }
  ary.size.odd? ? sorted[middle] : (sorted[middle]+sorted[middle-1])/2.0
end

def q25(ary)
  middle = (ary.size * 0.25).to_i
  sorted = ary.sort_by{ |a| a }
  ary.size.odd? ? sorted[middle] : (sorted[middle]+sorted[middle-1])/2.0
end

def q75(ary)
  middle = (ary.size * 0.75).to_i
  sorted = ary.sort_by{ |a| a }
  ary.size.odd? ? sorted[middle] : (sorted[middle]+sorted[middle-1])/2.0
end

module Enumerable

    def sum
      self.inject(0){|accum, i| accum + i }
    end
	
    def mean
      self.sum/self.length.to_f
    end

    def sample_variance
      m = self.mean
      sum = self.inject(0){|accum, i| accum +(i-m)**2 }
      sum/(self.length - 1).to_f
    end

    def standard_deviation
      return Math.sqrt(self.sample_variance)
    end
end

def mineCalc(k)
	pathTo = "EXP2-results-benchmark-disk"
	
	recordsPerSecond = []
	rpsLastTime = 0
	rpsCount = 0
	
	i = 1
	while (i < 11)
		index = 0
		File.open(pathTo + "/raw-".concat(i.to_s).concat("-10-").concat(k.to_s).concat(".csv"), "r") do |infile|
			while (line = infile.gets)
				if (index >= 1000000)
					timeForRecord = line[2..line.length()].to_i
					
					if (rpsLastTime + timeForRecord >= 1000000000)
						recordsPerSecond << rpsCount
						
						rpsCount = 0
						rpsLastTime = 1000000000 - rpsLastTime
					end

					while (timeForRecord > 1000000000)
						recordsPerSecond << 0
						
						timeForRecord -= 1000000000
					end

					rpsCount += 1
					rpsLastTime += timeForRecord
				end
				index += 1
			end
		end
		
		i += 1
	end

	recordsPerSecond << rpsCount
	
	File.open(pathTo + "/resultsThroughput-".concat(k.to_s).concat(".txt"), 'a') { |file| 
		file.puts(k.to_s)
		file.puts("Mean: ".concat(recordsPerSecond.mean.to_s))
		file.puts("CI: ".concat((1.96 * (recordsPerSecond.standard_deviation / Math.sqrt(recordsPerSecond.length).to_f)).to_s))
		file.puts("q25: ".concat(q25(recordsPerSecond).to_s))
		file.puts("Median: ".concat(median(recordsPerSecond).to_s))
		file.puts("q75: ".concat(q75(recordsPerSecond).to_s))
	}
end

puts "Start at #{Time.now}"
t1 = Thread.new {
	mineCalc(1)
}
t2 = Thread.new {
	mineCalc(2)
}
t3 = Thread.new {
	mineCalc(3)
}
t4 = Thread.new {
	mineCalc(4)
}
t5 = Thread.new {
	mineCalc(5)
}
t6 = Thread.new {
	mineCalc(6)
}

t1.join
t2.join
t3.join
t4.join
t5.join
t6.join
puts "End at #{Time.now}"