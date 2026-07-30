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
variable "resource_group_name" {
  description = "The name of the resource group"
  type        = string
  default     = "product-entreprise-student-rg"
}

variable "location" {
  description = "The Azure region where resources will be created"
  type        = string
  default     = "Central India"
}

variable "vm_size" {
  description = "The size of the virtual machine"
  type        = string
  default     = "Standard_B1s"
}

