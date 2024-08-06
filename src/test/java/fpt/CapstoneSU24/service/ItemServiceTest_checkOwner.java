package fpt.CapstoneSU24.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
@ExtendWith(MockitoExtension.class)

public class ItemServiceTest_checkOwner {
    @InjectMocks
    private ItemService itemService;
    @Test
    public void testCheckOwner_NullEmail() {
        assertFalse(itemService.checkOwner(null, "owner@example.com"));
    }
    //x
    @Test
    public void testCheckOwner_EmptyEmail() {
        assertFalse(itemService.checkOwner("", "owner@example.com"));
    }

    @Test
    public void testCheckOwner_NullCurrentOwnerEmail() {
        assertFalse(itemService.checkOwner("user@example.com", null));
    }

    @Test
    public void testCheckOwner_EmptyCurrentOwnerEmail() {
        assertFalse(itemService.checkOwner("user@example.com", ""));
    }

    @Test
    public void testCheckOwner_EmailsDoNotMatch() {
        assertFalse(itemService.checkOwner("user@example.com", "owner@example.com"));
    }

    @Test
    public void testCheckOwner_EmailsMatch() {
        assertTrue(itemService.checkOwner("owner@example.com", "owner@example.com"));
    }
}
