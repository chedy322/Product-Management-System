variable "ssh_key_path"{
    description = "Path to the SSH public key file"
    type        = string
    default     = "C:\\Users\\chedi\\Desktop\\allFolders\\myProjects\\productProject\\id_rsa.pub"
}


variable "my_ip_address" {
  description = "Your public IP address for SSH access"
  type        = string
  sensitive   = true
}