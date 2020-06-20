#!/usr/bin/env ruby

require 'English'

def run_command(command)
  system(command)
  system_status = $CHILD_STATUS.exitstatus
  if system_status != 0
    raise "#{command}, error code: #{system_status}"
  end
end

def replace_text_in_file(filename, matcher, replacement_text)
  text = File.read(filename)
  content = text.gsub(matcher, replacement_text)
  File.open(filename, "w") { |file| file << content }
end

def next_version(version_to_release)
  index = version_to_release.rindex('.')
  next_point_release = version_to_release[index].to_i + 1
  return "#{version_to_release[0..index]}#{next_point_release}-SNAPSHOT"
end

version_to_release = ARGV[0]
puts "Releasing #{version_to_release}"

run_command 'git checkout master && git pull'

replace_text_in_file 'gradle.properties', /PROJECT_VERSION=.*/, "PROJECT_VERSION=#{version_to_release}"
maven_dependency_prefix = "implementation 'com.jaynewstrom:concrete:"
replace_text_in_file 'README.md', /#{maven_dependency_prefix}.*/, "#{maven_dependency_prefix}#{version_to_release}'"

run_command "git add . && git commit -m \"Release version #{version_to_release}\""
run_command "git tag -a release-#{version_to_release} -m \"Release version #{version_to_release}\""

replace_text_in_file 'gradle.properties', /PROJECT_VERSION=.*/, "PROJECT_VERSION=#{next_version(version_to_release)}"
run_command "git add . && git commit -m \"Prepare for next development iteration\""

run_command "git push && git push --tags"
