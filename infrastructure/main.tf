resource "azurerm_resource_group" "rg" {
  name     = var.resource_group_name
  location = var.location
}


resource "azurerm_virtual_network" "vnet" {
  name                = "product-entreprise-student-vnet"
  address_space       = ["10.0.0.0/16"]
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name
}


# Configure the subnet to let the virtual network know about the subnet which lets the vm to communicate with the outside world
resource "azurerm_subnet" "app_subnet"{
  name                 = "product-entreprise-student-subnet"
  resource_group_name  = azurerm_resource_group.rg.name
  virtual_network_name = azurerm_virtual_network.vnet.name
  # Ip range with 24 bits 
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
    subnet_id                     = azurerm_subnet.app_subnet.id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.public_ip.id
  }
}


resource "azurerm_linux_virtual_machine" "vm" {
  name                = "product-entreprise-student-vm"
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  size                = var.vm_size
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


# Set up firewall secuirty rule (deny all traffic except for SSH(resctricted to my ip address) and HTTP)
resource "azurerm_network_security_group" "nsg"{
  name                = "product-entreprise-student-nsg"
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name
  security_rule {
    name                       = "SSH"
    priority                   = 100
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "22"
    source_address_prefix      = var.my_ip_address
    destination_address_prefix = "*"
  }
  security_rule{
    name="HTTP"
    priority                   = 101
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "80"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }
  security_rule{
    name="HTTPS"
    priority                   = 102
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "443"
    source_address_prefix      = "*"
    destination_address_prefix = "*"
  }
}

# Attach the firewall to the subnet
resource "azurerm_subnet_network_security_group_association" "subnet_nsg_association" {
  subnet_id                 = azurerm_subnet.app_subnet.id
  network_security_group_id = azurerm_network_security_group.nsg.id
}

