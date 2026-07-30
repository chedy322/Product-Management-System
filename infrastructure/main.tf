resource "azurerm_resource_group" "rg" {
  name     = "product-entreprise-student-rg"
  location = "Central India"
}


resource "azurerm_virtual_network" "vnet" {
  name                = "product-entreprise-student-vnet"
  address_space       = ["10.0.0.0/16"]
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name
}


# Configure the subnet to let the virtual network know about the subnet which lets the vm to communicate with the outside world
resource "azurerm_subnet" "subnet"{
  name                 = "product-entreprise-student-subnet"
  resource_group_name  = azurerm_resource_group.rg.name
  virtual_network_name = azurerm_virtual_network.vnet.name
  # Ip range with 24 bits to hosts NGinx and VM 
  address_prefixes     = ["10.0.2.0/24"]
}


resource "azurerm_public_ip" "public_ip" {
  name                = "product-entreprise-student-public-ip"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name
  allocation_method   = "Static"
  sku= "Standard"
  domain_name_label= "product-entreprise-student-public-ip"
}

resource "azurerm_network_interface" "nic" {
  name                = "product-entreprise-student-nic"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name

  ip_configuration {
    name                          = "internal"
    subnet_id                     = azurerm_subnet.subnet.id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.public_ip.id
  }
}


resource "azurerm_linux_virtual_machine" "vm" {
  name                = "product-entreprise-student-vm"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  size                = "Standard_B2ats_v2"
  admin_username      = "azureadminstudent"
  network_interface_ids = [
    azurerm_network_interface.nic.id,
  ]

  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
    name                 = "product-entreprise-server_OsDisk_1"
  }

  source_image_reference {
    publisher = "Canonical"
    offer     = "ubuntu-24_04-lts"
    sku       = "server"
    version   = "latest"
  }

  admin_ssh_key {
    username   = "azureadminstudent"
    public_key = file(var.ssh_key_path)
  }
}

output "public_ip_address" {
  value =azurerm_public_ip.public_ip.ip_address
}

# Add Auto shutdown to avoid unnecessary costs

resource "azurerm_dev_test_global_vm_shutdown_schedule" "auto_shutdown" {
  virtual_machine_id = azurerm_linux_virtual_machine.vm.id
  location           = azurerm_resource_group.rg.location
  enabled            = true

  daily_recurrence_time = "0300"
  timezone              = "E. Europe Standard Time"

  notification_settings {
    enabled         = true
    email= "chbouountito@gmail.com"
  }
}
# Deaasociate the public ip address which is associated with the vm to avoid unnecessary costs

# Delete the Static ip address once the vm is stopped to avoid unnecessary costs

