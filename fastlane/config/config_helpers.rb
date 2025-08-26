require_relative '../../fastlane-config/android_config'
require_relative '../../fastlane-config/ios_config'
require 'dotenv'
Dotenv.load('.env')

module FastlaneConfig
  # Move methods directly into FastlaneConfig module instead of nested Helpers module
  def self.get_android_signing_config(options = {})
    {
      storeFile: options[:store_file] || ENV['ANDROID_STORE_FILE'] || AndroidConfig::STORE_CONFIG[:default_store_file],
      storePassword: options[:store_password] || ENV['ANDROID_STORE_PASSWORD'] || AndroidConfig::STORE_CONFIG[:default_store_password],
      keyAlias: options[:key_alias] || ENV['ANDROID_KEY_ALIAS'] || AndroidConfig::STORE_CONFIG[:default_key_alias],
      keyPassword: options[:key_password] || ENV['ANDROID_KEY_PASSWORD'] || AndroidConfig::STORE_CONFIG[:default_key_password]
    }
  end

  def self.get_r2_config
    {
      access_key_id: ENV['R2_ACCESS_KEY_ID'] || AndroidConfig::R2_CONFIG[:access_key_id],
      secret_access_key: ENV['R2_SECRET_ACCESS_KEY'] || AndroidConfig::R2_CONFIG[:secret_access_key],
      bucket: ENV['R2_BUCKET'] || AndroidConfig::R2_CONFIG[:bucket],
      endpoint: ENV['R2_ENDPOINT'] || AndroidConfig::R2_CONFIG[:endpoint],
    }
  end
end